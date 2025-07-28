package com.bsit.uniread.application.services.chapter;

import com.bsit.uniread.application.dto.request.chapter.EditChapterRequest;
import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.chapter.ChapterStatus;
import com.bsit.uniread.domain.entities.paragraph.Paragraph;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.handler.publishers.chapter.ChapterEventPublisher;
import com.bsit.uniread.infrastructure.repositories.chapter.ChapterRepository;
import com.bsit.uniread.infrastructure.repositories.paragraph.ParagraphRepository;
import com.bsit.uniread.infrastructure.specifications.chapter.ChapterSpecification;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChapterService {

    private final ChapterEventPublisher publisher;
    private final BookService bookService;
    private final ChapterRepository chapterRepository;
    private final ParagraphRepository paragraphRepository;

    /**
     * Get the chapters of book
     * @param bookId
     * @param pageNo
     * @param pageSize
     * @param query
     * @param status
     * @param sortBy
     * @param orderBy
     * @param startDate
     * @param endDate
     * @param deletedAt
     * @return page of chaptes
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "chapters", key = "T(java.util.Objects).hash(#bookId, #pageNo, #pageSize, #query, #status, #sortBy, #orderBy, #startDate, #endDate, #deletedAt)")
    public Page<Chapter> getBookChapters(
            UUID bookId,
            int pageNo,
            int pageSize,
            String query,
            ChapterStatus status,
            String sortBy,
            String orderBy,
            String startDate,
            String endDate,
            String deletedAt
    ) {
        Sort.Direction direction = sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, orderBy);

        Specification<Chapter> chapterSpecification = Specification
                .where(ChapterSpecification.hasBookById(bookId))
                .and(ChapterSpecification.hasDeleted(deletedAt))
                .and(ChapterSpecification.hasStatus(status));

        return chapterRepository.findAll(chapterSpecification, PageRequest.of(pageNo, pageSize, sort));
    }

    @Cacheable(value = "chapter", key = "T(java.util.Objects).hash(#bookId, #chapterId")
    @Transactional(readOnly = true)
    public Chapter getBookChapterById(UUID bookId, UUID chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
    }

    /**
     * Get the chapter by using chapterId
     * @param chapterId
     * @return Chapter
     */
    @Transactional(readOnly = true)
    public Chapter getChapterById(UUID chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
    }

    /**
     * Create a new chapter
     * @param bookId
     * @param title
     * @return chapter
     */
    @Transactional
    @CacheEvict(value = "chapters", allEntries = true)
    public Chapter createNewChapter(UUID bookId, String title) {
        Book book = bookService.getBookById(bookId);
        Chapter chapter = Chapter.builder()
                .title(title)
                .book(book)
                .readCount(0L)
                .status(ChapterStatus.DRAFT)
                .build();

        chapterRepository.save(chapter);
        return chapter;
    }

    @Transactional
    public Chapter editBookChapterById(UUID bookId, UUID chapterId, EditChapterRequest request) {
        Chapter chapter = getBookChapterById(bookId, chapterId);

        List<Paragraph> paragraphs = request.getParagraphs()
                .stream()
                .map((p) -> Paragraph.builder()
                        .chapter(chapter)
                        .type(p.getType())
                        .content(p.getContent())
                        .alignment(p.getAlignment())
                        .createdAt(DateUtil.now())
                        .updatedAt(DateUtil.now())
                        .position(p.getPosition())
                        .build())
                .collect(Collectors.toList());

        chapter.setTitle(request.getTitle());
        chapter.setParagraphs(paragraphs);
        paragraphRepository.saveAll(paragraphs);
        return save(chapter);
    }

    @Transactional
    @CacheEvict(value = "chapters", allEntries = true)
    public Chapter publishChapter(UUID bookId, UUID chapterId, ChapterStatus status) {
        Chapter chapter = getBookChapterById(bookId, chapterId);
        chapter.setStatus(status);
        chapter.setPublishedAt(status == ChapterStatus.DRAFT ? null : DateUtil.now());
        Chapter newChapter = save(chapter);
        publisher.publishChapter(newChapter);
        return newChapter;
    }

    @Transactional
    public Chapter save(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    @Transactional
    @CacheEvict(value = "chapters", allEntries = true)
    public void deleteById(UUID bookId, UUID chapterId) {
        Chapter chapter = getBookChapterById(bookId, chapterId);
        chapter.setDeletedAt(DateUtil.now());
        save(chapter);
    }

    @Transactional
    @CacheEvict(value = "chapters", allEntries = true)
    public void forceDeleteById(UUID chapterId) {
        chapterRepository.deleteById(chapterId);
    }
}

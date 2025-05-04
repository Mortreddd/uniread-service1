package com.bsit.uniread.application.services.chapter;

import com.bsit.uniread.application.dto.request.chapter.EditChapterRequest;
import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.chapter.ChapterStatus;
import com.bsit.uniread.domain.entities.paragraph.Paragraph;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.handler.exceptions.chapter.InvalidOwnerException;
import com.bsit.uniread.infrastructure.repositories.chapter.ChapterRepository;
import com.bsit.uniread.infrastructure.repositories.paragraph.ParagraphRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChapterService {

    private final BookService bookService;
    private final ChapterRepository chapterRepository;
    private final ParagraphRepository paragraphRepository;

    /**
     * Get the chapters of book
     * @param bookId
     * @param pageNo
     * @param pageSize
     * @return page of chaptes
     */
    @Transactional(readOnly = true)
    public Page<Chapter> getBookChapters(UUID bookId, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Book book = bookService.getBookById(bookId);
        return chapterRepository.findByBook(book, pageable);
    }

    @Transactional(readOnly = true)
    public Chapter getBookChapterById(UUID bookId, UUID chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

        if(!Objects.equals(chapter.getBook().getId(), bookId)) {
            throw new InvalidOwnerException("The selected chapter is not the owner of the book");
        }

        return chapter;
    }

    /**
     * Create a new chapter
     * @param bookId
     * @param title
     * @return chapter
     */
    @Transactional
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
        if(!Objects.equals(chapter.getBook().getId(), bookId)) {
            throw new InvalidOwnerException("The selected chapter is not the owner of the book");
        }

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
        return saveChapter(chapter);
    }

    @Transactional
    public Chapter saveChapter(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    @Transactional
    public void deleteById(UUID chapterId) {
        chapterRepository.deleteById(chapterId);
    }
}

package com.bsit.uniread.application.services.paragraph;

import com.bsit.uniread.application.services.chapter.ChapterService;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.paragraph.Paragraph;
import com.bsit.uniread.infrastructure.repositories.paragraph.ParagraphRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParagraphService {

    private final ParagraphRepository paragraphRepository;
    private final ChapterService chapterService;

    /**
     * Get all the paragraphs of the chapter
     * @param bookId
     * @param chapterId
     * @return list of paragraph
     */
    @Transactional(readOnly = true)
    public List<Paragraph> getBookChapterParagraphs(UUID bookId, UUID chapterId) {
        Chapter chapter = chapterService.getBookChapterById(bookId, chapterId);
        return paragraphRepository.findByChapter(chapter);
    }
}

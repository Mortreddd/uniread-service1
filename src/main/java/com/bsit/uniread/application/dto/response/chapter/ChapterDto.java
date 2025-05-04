package com.bsit.uniread.application.dto.response.chapter;

import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.dto.response.paragraph.ParagraphDto;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.paragraph.Paragraph;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ChapterDto {

    private UUID id;
    private BookDto book;
    private String title;
    private List<ParagraphDto> paragraphs;

    private Long readCount;
    private Boolean isPublished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ChapterDto(Chapter chapter) {
        this.id = chapter.getId();
        this.book = new BookDto(chapter.getBook());
        this.title = chapter.getTitle();
        this.paragraphs = chapter.getParagraphs().stream().map(ParagraphDto::new).toList();
        this.readCount = chapter.getReadCount();
        this.isPublished = chapter.getIsPublished();
        this.createdAt = chapter.getCreatedAt();
        this.updatedAt = chapter.getUpdatedAt();
    }
}

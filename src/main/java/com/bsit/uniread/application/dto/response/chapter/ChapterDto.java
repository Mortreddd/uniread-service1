package com.bsit.uniread.application.dto.response.chapter;

import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.dto.response.paragraph.ParagraphDto;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.chapter.ChapterStatus;
import com.bsit.uniread.domain.entities.paragraph.Paragraph;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ChapterDto {

    private UUID id;
    private UUID bookId;
    private String title;
    private List<ParagraphDto> paragraphs;
    private Long readCount;
    private ChapterStatus status;
    private Boolean isPublished;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public ChapterDto(Chapter chapter) {
        this.id = chapter.getId();
        this.bookId = chapter.getBook().getId();
        this.title = chapter.getTitle();
        this.paragraphs = chapter.getParagraphs().stream().map(ParagraphDto::new).toList();
        this.readCount = chapter.getReadCount();
        this.status = chapter.getStatus();
        this.isPublished = chapter.getIsPublished();
        this.publishedAt = chapter.getPublishedAt();
        this.createdAt = chapter.getCreatedAt();
        this.updatedAt = chapter.getUpdatedAt();
        this.deletedAt = chapter.getDeletedAt();
    }
}

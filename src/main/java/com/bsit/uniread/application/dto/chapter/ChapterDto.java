package com.bsit.uniread.application.dto.chapter;

import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChapterDto {

    private UUID id;
    private BookDto book;
    private String title;
    private String content;

    private Long readCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ChapterDto(Chapter chapter) {
        this.id = chapter.getId();
        this.book = new BookDto(chapter.getBook());
        this.title = chapter.getTitle();
        this.content = chapter.getContent();
        this.readCount = chapter.getReadCount();
        this.createdAt = chapter.getCreatedAt();
        this.updatedAt = chapter.getUpdatedAt();
    }
}

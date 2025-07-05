package com.bsit.uniread.application.dto.response.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import lombok.Data;

import java.util.UUID;

/**
 * Simple information for the book
 */
@Data
public class SimpleBookInfoDto {

    private UUID id;
    private String title;
    private String coverPhoto;
    private String description;
    private BookStatus status;
    private Integer readCount;
    private Boolean completed;
    private Boolean matured;

    public SimpleBookInfoDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.coverPhoto = book.getCoverPhoto();
        this.description = book.getDescription();
        this.status = book.getStatus();
        this.readCount = book.getReadCount();
        this.completed = book.getCompleted();
        this.matured = book.getMatured();
    }

}

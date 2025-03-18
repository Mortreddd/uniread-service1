package com.bsit.uniread.application.dto.response.book;

import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookComment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookCommentDto {

    private UUID id;
    private BookDto book;
    private AuthorDto user;
    private BookCommentDto parentBookComment;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookCommentDto(BookComment bookComment) {
        this.id = bookComment.getId();
        this.book = new BookDto(bookComment.getBook());
        this.user = new AuthorDto(bookComment.getUser());
        this.parentBookComment = new BookCommentDto(bookComment.getParentBookComment());
        this.content = bookComment.getContent();
        this.createdAt = bookComment.getCreatedAt();
        this.updatedAt = bookComment.getUpdatedAt();
    }
}

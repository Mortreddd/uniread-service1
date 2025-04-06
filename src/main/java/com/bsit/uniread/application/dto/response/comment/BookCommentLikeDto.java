package com.bsit.uniread.application.dto.response.comment;

import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.domain.entities.book.BookCommentLike;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookCommentLikeDto {

    private UUID id;
    private BookCommentDto bookComment;
    private AuthorDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookCommentLikeDto(BookCommentLike bookCommentLike) {
        this.id = bookCommentLike.getId();
        this.bookComment = new BookCommentDto(bookCommentLike.getBookComment());
        this.user = new AuthorDto(bookCommentLike.getUser());
        this.createdAt = bookCommentLike.getCreatedAt();
        this.updatedAt = bookCommentLike.getUpdatedAt();

    }

}

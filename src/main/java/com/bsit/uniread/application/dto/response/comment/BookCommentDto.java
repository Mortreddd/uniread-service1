package com.bsit.uniread.application.dto.response.comment;

import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.domain.entities.book.BookComment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class BookCommentDto {

    private UUID id;
    private BookDto book;
    private AuthorDto user;
    private BookCommentDto parentComment;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public List<BookCommentLikeDto> bookCommentLikes;
    private Long totalLikes;

    public BookCommentDto(BookComment bookComment) {
        this.id = bookComment.getId();
        this.book = new BookDto(bookComment.getBook());
        this.user = new AuthorDto(bookComment.getUser());
        this.parentComment = new BookCommentDto(bookComment.getParentBookComment());
        this.rating = bookComment.getRating();
        this.content = bookComment.getContent();
        this.createdAt = bookComment.getCreatedAt();
        this.updatedAt = bookComment.getUpdatedAt();
        this.totalLikes = bookComment.getTotalLikes();
    }
}

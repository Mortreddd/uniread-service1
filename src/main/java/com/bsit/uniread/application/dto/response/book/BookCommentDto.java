package com.bsit.uniread.application.dto.response.book;

import com.bsit.uniread.application.dto.response.reaction.BookCommentReactionDto;
import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookComment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class BookCommentDto {

    private UUID id;
    private SimpleBookInfoDto book;
    private AuthorDto user;
    private Integer rating;
    private BookCommentDto parentBookComment;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BookCommentReactionDto> reactions;

    public BookCommentDto(BookComment bookComment) {
        this.id = bookComment.getId();
        this.book = new SimpleBookInfoDto(bookComment.getBook());
        this.rating = bookComment.getRating();
        this.user = new AuthorDto(bookComment.getUser());

        if(bookComment.getParentBookComment() != null) {
            this.parentBookComment = new BookCommentDto(bookComment.getParentBookComment());
        } else {
            this.parentBookComment = null;
        }

        this.content = bookComment.getContent();
        this.createdAt = bookComment.getCreatedAt();
        this.updatedAt = bookComment.getUpdatedAt();
        this.reactions = bookComment.getBookCommentLikes()
                .stream()
                .map(BookCommentReactionDto::new)
                .toList();
    }
}

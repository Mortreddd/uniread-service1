package com.bsit.uniread.application.dto.response.reaction;

import com.bsit.uniread.domain.entities.Reaction;
import com.bsit.uniread.domain.entities.book.BookCommentLike;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BookCommentLikeDto {

    private UUID id;
    private UUID bookCommentId;
    private UUID userId;
    private Reaction reaction;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookCommentLikeDto(BookCommentLike bookCommentLike) {
        this.id = bookCommentLike.getId();
        this.bookCommentId = bookCommentLike.getBookComment().getId();
        this.userId = bookCommentLike.getUser().getId();
        this.reaction = bookCommentLike.getReaction();
        this.createdAt = bookCommentLike.getCreatedAt();
        this.updatedAt = bookCommentLike.getUpdatedAt();
    }
}

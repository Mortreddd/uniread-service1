package com.bsit.uniread.application.dto.response.reaction;

import com.bsit.uniread.domain.entities.Reaction;
import com.bsit.uniread.domain.entities.book.BookCommentLike;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BookCommentReactionDto {

    private UUID id;
    private UUID bookCommentId;
    private UUID userId;
    private Reaction reaction;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookCommentReactionDto(BookCommentLike like) {
        this.id = like.getId();
        this.bookCommentId = like.getBookComment().getId();
        this.userId = like.getUser().getId();
        this.reaction = like.getReaction();
        this.createdAt = like.getCreatedAt();
        this.updatedAt = like.getUpdatedAt();
    }

}

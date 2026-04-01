package com.bsit.uniread.domain.entities.paragraph;

import com.bsit.uniread.domain.entities.Reaction;
import com.bsit.uniread.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Data
@Table(name = "paragraph_comment_likes", indexes = {
        @Index(name = "idx_paragraph_comment_likes_paragraph_comment_id", columnList = "paragraph_comment_id"),
        @Index(name = "idx_paragraph_comment_likes_user_id", columnList = "user_id")
})
@Entity
@Builder
public class ParagraphCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(targetEntity = ParagraphComment.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "paragraph_comment_id")
    @JsonBackReference
    private ParagraphComment paragraphComment;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Enumerated(EnumType.STRING)
    private Reaction reaction;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

}

package com.bsit.uniread.domain.entities.book;

import com.bsit.uniread.domain.entities.Reaction;
import com.bsit.uniread.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * A table entity for the likes for a book comment
 */
@Getter
@Setter
@Builder
@Table(name = "book_comment_likes", indexes = {
        @Index(name = "idx_book_comment_likes_book_comment_id", columnList = "book_comment_id"),
        @Index(name = "idx_book_comment_likes_user_id", columnList = "user_id")
})
@Entity
public class BookCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "book_comment_id")
    @JsonBackReference
    private BookComment bookComment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @Enumerated(EnumType.STRING)
    private Reaction reaction;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}

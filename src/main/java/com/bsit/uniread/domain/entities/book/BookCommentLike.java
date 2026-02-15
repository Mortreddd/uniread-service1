package com.bsit.uniread.domain.entities.book;

import com.bsit.uniread.domain.entities.Reaction;
import com.bsit.uniread.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A table entity for the likes for a book comment
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

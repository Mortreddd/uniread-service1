package com.bsit.uniread.domain.entities.book;

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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A table for the ratings, comments of users for the book
 */
@Entity
@Table(name = "book_comments", indexes = {
    @Index(name = "idx_book_comments_book_id", columnList = "book_id"),
    @Index(name = "idx_book_comments_user_id", columnList = "user_id"),
    @Index(name = "idx_book_comments_parent_book_comment_id", columnList = "parent_book_comment_id"),
    @Index(name = "idx_book_comments_created_at", columnList = "created_at"),
})
@Getter
@Setter
@Builder
public class BookComment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "parent_book_comment_id")
    @JsonManagedReference
    private BookComment parentBookComment;

    private String content;

    @Builder.Default
    @OneToMany(mappedBy = "bookComment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<BookCommentLike> bookCommentLikes = new ArrayList<>();

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;


}

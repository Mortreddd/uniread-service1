package com.bsit.uniread.domain.entities.book;

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
    @Index(name = "idx_book_comments_parent_book_comment_id", columnList = "parent_book_comment_id")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookComment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_book_comment_id")
    @JsonManagedReference
    private BookComment parentBookComment;

    private Integer rating;
    private String content;

    @Builder.Default
    @OneToMany(mappedBy = "bookComment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<BookCommentLike> bookCommentLikes = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Transient
    private Long totalLikes;

    public Long getTotalLikes() {
        return (long) bookCommentLikes.size();
    }

}

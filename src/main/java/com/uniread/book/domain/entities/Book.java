package com.uniread.book.domain.entities;

import com.uniread.auth.domain.entities.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "books", indexes = {
        @Index(name = "idx_books_title", columnList = "title"),
        @Index(name = "idx_books_user_id", columnList = "user_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String coverPhoto;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private Integer readCount = 0;
    @Builder.Default
    private Boolean completed = false;
    @Builder.Default
    private Boolean matured = false;

    @Builder.Default
    private Long ratingCount = 0L;
    @Builder.Default
    private Long likeCount = 0L;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_genres",
            joinColumns = { @JoinColumn(name = "book_id")},
            inverseJoinColumns = { @JoinColumn(name = "genre_id") },
            indexes = {
                @Index(name = "idx_book_genres_book_id", columnList = "book_id"),
                @Index(name = "idx_book_genres_genre_id", columnList = "genre_id")
            }
    )
    private Set<Genre> genres = new HashSet<>();

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Instant deletedAt;

    private Instant bannedAt;

    private Instant publishedAt;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private List<Chapter> chapters = new ArrayList<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_tags", joinColumns = {
            @JoinColumn(name = "book_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "tag_id")
    }, indexes = {
            @Index(name = "idx_book_tags_book_id", columnList = "book_id"),
            @Index(name = "idx_book_tags_tag_id", columnList = "tag_id")
    })
    private Set<Tag> tags = new HashSet<>();

}

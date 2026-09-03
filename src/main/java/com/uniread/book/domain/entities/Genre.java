package com.uniread.book.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "genres")
@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;

    @Builder.Default
    private Long bookCount = 0L;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "book_genres",
            joinColumns = { @JoinColumn(name = "genre_id")},
            inverseJoinColumns = { @JoinColumn(name = "book_id")},
            indexes = {
            @Index(name = "idx_book_genres_book_id", columnList = "book_id"),
            @Index(name = "idx_book_genres_genre_id", columnList = "genre_id")
    })
    public List<Book> books;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
}

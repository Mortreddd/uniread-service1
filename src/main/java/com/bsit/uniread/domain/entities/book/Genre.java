package com.bsit.uniread.domain.entities.book;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@Table(name = "genres")
@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;

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

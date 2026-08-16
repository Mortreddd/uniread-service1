package com.uniread.user.domain.entities;

import com.uniread.auth.domain.entities.User;
import com.uniread.book.domain.entities.Book;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "Library")
@Table(name = "libraries", indexes = {
        @Index(name = "idx_libraries_user_id", columnList = "user_id"),
        @Index(name = "idx_libraries_book_id", columnList = "book_id")
})
@Builder
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "book_id")
    private Book book;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}

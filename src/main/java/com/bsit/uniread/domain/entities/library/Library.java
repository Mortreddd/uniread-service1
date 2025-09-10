package com.bsit.uniread.domain.entities.library;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "idx_user_library_id", columnList="user_id"),
        @Index(name = "idx_book_library_id", columnList="book_id")
})
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "library", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "library", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Book book;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

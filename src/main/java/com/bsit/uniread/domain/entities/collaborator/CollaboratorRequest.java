package com.bsit.uniread.domain.entities.collaborator;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Table(name = "collaborator_requests", indexes = {
        @Index(name = "idx_collaborator_requests_user_id", columnList = "user_id"),
        @Index(name = "idx_collaborator_requests_book_id", columnList = "book_id")
})
@Entity
@Setter
@Getter
@Builder
public class CollaboratorRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;

    @Enumerated(EnumType.STRING)
    private CollaboratorRequestStatus status;

    private String message;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}

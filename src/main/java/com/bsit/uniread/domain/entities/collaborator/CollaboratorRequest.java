package com.bsit.uniread.domain.entities.collaborator;

import com.bsit.uniread.application.controllers.collaborator.RequestStatus;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "collaborator_requests", indexes = {
        @Index(name = "idx_collaborator_requests_user_id", columnList = "user_id"),
        @Index(name = "idx_collaborator_requests_book_id", columnList = "book_id")
})
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String message;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

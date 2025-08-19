package com.bsit.uniread.domain.entities;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.collaborator.CollaboratorPermission;
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
import java.util.*;

@Table(name = "collaborator", indexes = {
        @Index(name = "idx_book_id", columnList = "book_id"),
        @Index(name = "idx_user_id", columnList = "user_id"),
})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private List<CollaboratorPermission> permissions = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime bannedAt;

}

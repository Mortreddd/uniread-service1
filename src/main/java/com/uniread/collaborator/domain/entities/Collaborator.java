package com.uniread.collaborator.domain.entities;

import com.uniread.book.domain.entities.Book;
import com.uniread.auth.domain.entities.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.*;

@Table(name = "collaborators", indexes = {
        @Index(name = "idx_collaborators_book_id", columnList = "book_id"),
        @Index(name = "idx_collaborators_user_id", columnList = "user_id"),
})
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private List<CollaboratorPermission> permissions = new ArrayList<>();

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Instant unbannedAt;
    private Instant bannedAt;
}

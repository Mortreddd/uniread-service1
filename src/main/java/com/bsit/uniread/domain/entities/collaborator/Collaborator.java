package com.bsit.uniread.domain.entities.collaborator;

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

    private LocalDateTime unbannedAt;
    private LocalDateTime bannedAt;

    @Transient
    private boolean isAdmin;
    @Transient
    private boolean canEditBook;
    @Transient
    private boolean canDeleteBook;
    @Transient
    private boolean canPublishBook;

    @Transient
    private boolean canAddChapter;
    @Transient
    private boolean canEditChapter;
    @Transient
    private boolean canPublishChapter;

    @Transient
    private boolean canAddCollaborator;
    @Transient
    private boolean canModifyPermissions;

    public boolean getIsAdmin() {
        return this.getBook().getUser().getId().equals(this.getUser().getId());
    }

    public boolean getCanEditBook() {
        return this.permissions.stream().anyMatch(p -> p.equals(CollaboratorPermission.EDIT_BOOK));
    }


    public boolean getCanDeleteBook() {
        return this.permissions.stream().anyMatch(p -> p.equals(CollaboratorPermission.DELETE_BOOK));
    }

    public boolean getCanPublishBook() {
        return this.permissions.stream().anyMatch(p -> p.equals(CollaboratorPermission.PUBLISH_BOOK));
    }

    public boolean getCanAddChapter() {
        return this.permissions.stream().anyMatch(p -> p.equals(CollaboratorPermission.ADD_CHAPTER));
    }

    public boolean getCanEditChapter() {
        return this.permissions.stream().anyMatch(p -> p.equals(CollaboratorPermission.EDIT_CHAPTER));
    }

    public boolean getCanPublishChapter() {
        return this.permissions.stream().anyMatch(p -> p.equals(CollaboratorPermission.PUBLISH_CHAPTER));
    }

    public boolean getCanAddCollaborator() {
        return this.permissions.stream().anyMatch(p -> p.equals(CollaboratorPermission.ADD_COLLABORATOR));
    }

    public boolean getCanModifyPermissions() {
        return this.permissions.stream().anyMatch(p -> p.equals(CollaboratorPermission.MODIFY_PERMISSIONS));
    }


}

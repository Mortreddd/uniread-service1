package com.uniread.book.domain.entities;

import com.uniread.collaborator.domain.entities.Collaborator;
import com.uniread.collaborator.domain.entities.CollaboratorRequest;
import com.uniread.social.domain.entities.BookComment;
import com.uniread.social.domain.entities.BookLike;
import com.uniread.user.domain.entities.Library;
import com.uniread.auth.domain.entities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @JsonBackReference
    private User user;

    private String title;
    private String coverPhoto;
    private String description;
    private Integer readCount;
    private Boolean completed;
    private Boolean matured;

    @Builder.Default
    private Long ratingCount = 0L;
    @Builder.Default
    private Long likeCount = 0L;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_genres",
            joinColumns = { @JoinColumn(name = "book_id")},
            inverseJoinColumns = { @JoinColumn(name = "genre_id")})
    @JsonManagedReference
    private List<Genre> genres;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Instant deletedAt;

    private Instant bannedAt;

    private Instant publishedAt;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @JsonManagedReference
    private List<Chapter> chapters = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<BookComment> bookComments = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<BookLike> bookLikes = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @JsonManagedReference
    private List<Collaborator> collaborators = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @JsonManagedReference
    private List<CollaboratorRequest> collaboratorRequests = new ArrayList<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_tags", joinColumns = {
            @JoinColumn(name = "book_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "tag_id")
    })
    @JsonManagedReference
    private List<Tag> tags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Library> libraries = new ArrayList<>();

}

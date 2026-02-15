package com.bsit.uniread.domain.entities.book;

import com.bsit.uniread.domain.entities.collaborator.Collaborator;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.chapter.ChapterStatus;
import com.bsit.uniread.domain.entities.collaborator.CollaboratorRequest;
import com.bsit.uniread.domain.entities.library.Library;
import com.bsit.uniread.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "books", indexes = {
        @Index(name = "idx_books_title", columnList = "title"),
        @Index(name = "idx_books_user_id", columnList = "user_id")
})
@Data
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

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_genres",
            joinColumns = { @JoinColumn(name = "book_id")},
            inverseJoinColumns = { @JoinColumn(name = "genre_id")})
    @JsonManagedReference
    private List<Genre> genres;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private LocalDateTime bannedAt;

    private LocalDateTime publishedAt;

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
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    @JsonManagedReference
    private List<Tag> tags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Library> libraries = new ArrayList<>();

    @Transient
    public Boolean isPublished;

    @Transient
    private Integer totalChapterPublishedCount;

    @Transient
    private Integer totalChapterDraftsCount;

    @Transient
    private Integer totalChaptersCount;

    @Transient
    private Long totalLikesCount;

    @Transient
    private Long totalReadsCount;

    @Transient
    private Long totalRatingsCount;

    public Boolean getIsPublished() {
        return status == BookStatus.PUBLISHED;
    }

    public Integer getTotalChaptersCount() {
        return chapters.size();
    }

    public Long getTotalLikesCount() {
        return (long) bookLikes.size();
    }

    public Long getTotalRatingsCount() {
        return bookComments
                .stream()
                .mapToLong(BookComment::getRating)
                .sum();
    }

    public Long getTotalReadsCount() {
        return chapters
                .stream()
                .mapToLong(Chapter::getReadCount)
                .sum();
    }

    public Integer getTotalChapterPublishedCount() {
        return Math.toIntExact(chapters
                .stream()
                .filter((chapter) -> chapter.getStatus() == ChapterStatus.PUBLISHED)
                .count());
    }

    public Integer getTotalChapterDraftsCount() {
        return Math.toIntExact(chapters
                .stream()
                .filter((chapter) -> chapter.getStatus() == ChapterStatus.DRAFT)
                .count());
    }
}

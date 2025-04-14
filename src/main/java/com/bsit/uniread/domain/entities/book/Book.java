package com.bsit.uniread.domain.entities.book;

import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.chapter.ChapterStatus;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.entities.book.BookStatus;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_user_id", columnList = "user_id")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
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
    @JoinTable(name = "book_genres")
    @JsonManagedReference
    private List<Genre> genres;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;

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
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    @JsonManagedReference
    private List<Tag> tags = new ArrayList<>();

    public Boolean isMatured(){
        return matured;
    }

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

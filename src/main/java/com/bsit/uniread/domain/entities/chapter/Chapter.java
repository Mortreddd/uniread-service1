package com.bsit.uniread.domain.entities.chapter;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.paragraph.Paragraph;
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
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "chapters", indexes = {
        @Index(name = "idx_chapters_book_id", columnList = "book_id"),
        @Index(name = "idx_chapters_status", columnList = "status")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;

    private String title;

    @Enumerated(EnumType.STRING)
    private ChapterStatus status;

    private Long readCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private LocalDateTime publishedAt;

    @Transient
    private Boolean isPublished;

    public Boolean getIsPublished() {
        return status == ChapterStatus.PUBLISHED;
    }

    @Builder.Default
    @OneToMany(targetEntity = Paragraph.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "paragraph_id")
    @JsonManagedReference
    private List<Paragraph> paragraphs = new ArrayList<>();

    @Builder.Default
    @OneToMany(targetEntity = ChapterComment.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chapter_comment_id")
    @JsonManagedReference
    private List<ChapterComment> chapterComments = new ArrayList<>();

    @Builder.Default
    @OneToMany(targetEntity = ChapterComment.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chapter_like_id")
    @JsonManagedReference
    private List<ChapterLike> chapterLikes = new ArrayList<>();

}

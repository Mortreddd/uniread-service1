package com.bsit.uniread.domain.entities.chapter;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.paragraph.Paragraph;
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
@Table(name = "chapters", indexes = {
        @Index(name = "idx_chapters_book_id", columnList = "book_id"),
        @Index(name = "idx_chapters_status", columnList = "status")
})
@Getter
@Setter
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

    @Builder.Default
    private Long readCount = 0L;

    @Builder.Default
    private Long likesCount = 0L;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Instant deletedAt;

    private Instant publishedAt;
    private Instant unpublishedAt;

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
    @OneToMany(targetEntity = ChapterLike.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chapter_like_id")
    @JsonManagedReference
    private List<ChapterLike> chapterLikes = new ArrayList<>();

}

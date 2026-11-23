package com.uniread.book.domain.entities;

import com.uniread.social.domain.entities.ChapterComment;
import com.uniread.social.domain.entities.ChapterLike;
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
        @Index(name = "idx_chapters_volume_id", columnList = "volume_id"),
        @Index(name = "idx_chapters_status", columnList = "status")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(targetEntity = Volume.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "volume_id")
    private Volume volume;

    private String title;
    @Column(name = "chapter_number", nullable = false)
    private Integer chapterNumber;

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
    private List<Paragraph> paragraphs = new ArrayList<>();

    @Builder.Default
    @OneToMany(targetEntity = ChapterComment.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chapter_comment_id")
    private List<ChapterComment> chapterComments = new ArrayList<>();

    @Builder.Default
    @OneToMany(targetEntity = ChapterLike.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chapter_like_id")
    private List<ChapterLike> chapterLikes = new ArrayList<>();

}

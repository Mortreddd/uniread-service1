package com.bsit.uniread.domain.entities.chapter;

import com.bsit.uniread.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "chapter_comments", indexes = {
        @Index(name = "idx_chapter_comments_chapter_id", columnList = "chapter_id"),
        @Index(name = "idx_chapter_comments_user_id", columnList = "user_id"),
        @Index(name = "idx_chapter_comments_parent_chapter_comment_id", columnList = "parent_chapter_comment_id")
})
@Entity
@Builder
public class ChapterComment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(targetEntity = Chapter.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "chapter_id")
    @JsonBackReference
    private Chapter chapter;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_chapter_comment_id")
    @JsonManagedReference
    private ChapterComment parentChapterComment;

    private String content;

    @Builder.Default
    private Long likesCount = 0L;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "chapterComment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<ChapterCommentLike> chapterCommentLikes = new ArrayList<>();

}

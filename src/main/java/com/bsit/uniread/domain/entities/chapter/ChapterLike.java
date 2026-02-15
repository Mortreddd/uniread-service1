package com.bsit.uniread.domain.entities.chapter;

import com.bsit.uniread.domain.entities.Reaction;
import com.bsit.uniread.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chapter_likes", indexes = {
        @Index(name = "idx_chapter_likes_chapter_id", columnList = "chapter_id"),
        @Index(name = "idx_chapter_likes_user_id", columnList = "user_id")
})
@Entity
public class ChapterLike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(targetEntity = Chapter.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id", unique = true)
    private Chapter chapter;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Enumerated(EnumType.STRING)
    private Reaction reaction;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}

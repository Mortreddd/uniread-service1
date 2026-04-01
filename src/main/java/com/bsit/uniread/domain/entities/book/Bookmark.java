package com.bsit.uniread.domain.entities.book;

import com.bsit.uniread.domain.entities.paragraph.Paragraph;
import com.bsit.uniread.domain.entities.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Table(name = "bookmarks", indexes = {
        @Index(name = "idx_paragraphs_paragraph_id", columnList = "paragraph_id"),
        @Index(name = "idx_paragraphs_user_id", columnList = "user_id")
}, uniqueConstraints = @UniqueConstraint(columnNames = {"paragraph_id", "user_id"}))
@Entity
@Builder
@Getter
@Setter
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "paragraph_id")
    private Paragraph paragraph;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}

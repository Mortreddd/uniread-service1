package com.uniread.book.domain.entities;

import com.uniread.auth.domain.entities.User;
import jakarta.persistence.*;
import lombok.*;
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
@AllArgsConstructor
@NoArgsConstructor
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

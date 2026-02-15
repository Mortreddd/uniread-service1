package com.bsit.uniread.domain.entities.book;

import com.bsit.uniread.domain.entities.paragraph.Paragraph;
import com.bsit.uniread.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
import java.util.UUID;

@Table(name = "bookmarks", indexes = {
        @Index(name = "idx_paragraphs_paragraph_id", columnList = "paragraph_id"),
        @Index(name = "idx_paragraphs_user_id", columnList = "user_id")
}, uniqueConstraints = @UniqueConstraint(columnNames = {"paragraph_id", "user_id"}))
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "paragraph_id")
    @JsonBackReference
    private Paragraph paragraph;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

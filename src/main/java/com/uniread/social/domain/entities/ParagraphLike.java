package com.uniread.social.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.uniread.book.domain.entities.Paragraph;
import com.uniread.user.domain.entities.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "paragraph_likes", indexes = {
        @Index(name = "idx_paragraph_likes_paragraph_id", columnList = "paragraph_id"),
        @Index(name = "idx_paragraph_likes_user_id", columnList = "user_id")
})
@Entity
@Builder
public class ParagraphLike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(targetEntity = Paragraph.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "paragraph_id")
    @JsonBackReference
    private Paragraph paragraph;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Enumerated(EnumType.STRING)
    private Reaction reaction;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

}

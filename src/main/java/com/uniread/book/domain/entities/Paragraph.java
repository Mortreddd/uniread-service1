package com.uniread.book.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.uniread.social.domain.entities.ParagraphComment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "paragraphs", indexes = {
        @Index(name = "idx_paragraphs_chapter_id", columnList = "chapter_id")
})
@Entity
@Builder
public class Paragraph {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "chapter_id")
    @JsonBackReference
    private Chapter chapter;

    private String type;

    private String alignment;

    private Integer position;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "paragraph", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<ParagraphComment> paragraphComments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "paragraph", cascade = CascadeType.MERGE)
    private List<Bookmark> bookmarks = new ArrayList<>();

}

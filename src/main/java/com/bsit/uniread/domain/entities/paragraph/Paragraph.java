package com.bsit.uniread.domain.entities.paragraph;

import com.bsit.uniread.domain.entities.book.Bookmark;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.UUID;

@Data
@Table(name = "paragraphs", indexes = {
        @Index(name = "idx_paragraphs_chapter_id", columnList = "chapter_id")
})
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "paragraph", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<ParagraphComment> paragraphComments = new ArrayList<>();

    @OneToMany(mappedBy = "paragraph", cascade = CascadeType.REMOVE)
    private List<Bookmark> bookmarks = new ArrayList<>();

}

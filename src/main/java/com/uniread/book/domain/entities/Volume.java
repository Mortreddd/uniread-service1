package com.uniread.book.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "volumes", indexes = {
        @Index(name = "idx_volumes_number", columnList = "volume_number"),
        @Index(name = "idx_volumes_title", columnList = "title"),
        @Index(name = "idx_volumes_status", columnList = "status")
})
@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class Volume {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "volume_number", nullable = false)
    private Integer volumeNumber;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private String coverUrl;
    private String coverPublicId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private VolumeStatus status = VolumeStatus.DRAFT;

    @Builder.Default
    private Integer chaptersCount = 0;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Instant deletedAt;

    private Instant publishedAt;

    @Builder.Default
    @OneToMany(
            mappedBy = "volume",
            fetch = FetchType.LAZY
    )
    @OrderBy("chapterNumber ASC")
    private List<Chapter> chapters = new ArrayList<>();
}

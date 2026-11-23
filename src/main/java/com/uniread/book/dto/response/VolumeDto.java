package com.uniread.book.dto.response;


import com.uniread.book.domain.entities.VolumeStatus;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class VolumeDto {
    private UUID id;

    private UUID bookId;
    private Integer volumeNumber;
    private String title;

    private String description;

    private String coverUrl;
    private String coverPublicId;

    private VolumeStatus status;

    private Integer chaptersCount;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private Instant publishedAt;
}

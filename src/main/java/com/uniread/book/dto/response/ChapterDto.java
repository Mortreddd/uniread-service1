package com.uniread.book.dto.response;

import com.uniread.book.domain.entities.ChapterStatus;
import com.uniread.book.domain.entities.Volume;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
public class ChapterDto {

    private UUID id;

    private UUID volumeId;

    private Integer chapterNumber;

    private String title;

    private ChapterStatus status;

    private Long readCount;
    private Long likesCount;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;

    private Instant publishedAt;
    private Instant unpublishedAt;

}

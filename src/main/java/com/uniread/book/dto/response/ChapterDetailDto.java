package com.uniread.book.dto.response;

import com.uniread.book.domain.entities.ChapterStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ChapterDetailDto {


    private UUID id;

    private UUID volumeId;

    private Integer chapterNumber;

    private ChapterStatus status;

    private Long readCount;
    private Long likesCount;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;

    private Instant publishedAt;
    private Instant unpublishedAt;

    public List<ParagraphDto> content;

}

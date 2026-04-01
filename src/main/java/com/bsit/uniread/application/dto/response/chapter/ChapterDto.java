package com.bsit.uniread.application.dto.response.chapter;

import com.bsit.uniread.application.dto.response.paragraph.ParagraphDto;
import com.bsit.uniread.domain.entities.chapter.ChapterStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChapterDto {

    private UUID id;
    private UUID bookId;
    private String title;
    private List<ParagraphDto> paragraphs;
    private Long readCount;
    private ChapterStatus status;
    private Instant publishedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

}

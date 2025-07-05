package com.bsit.uniread.application.dto.response.paragraph;

import com.bsit.uniread.domain.entities.paragraph.Paragraph;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ParagraphDto {

    private UUID id;
    private UUID chapterId;
    private String type;
    private String alignment;
    private Integer position;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ParagraphDto(Paragraph paragraph) {
        this.id = paragraph.getId();
        this.chapterId = paragraph.getChapter().getId();
        this.type = paragraph.getType();
        this.alignment = paragraph.getAlignment();
        this.content = paragraph.getContent();
        this.createdAt = paragraph.getCreatedAt();
        this.updatedAt = paragraph.getUpdatedAt();
    }
}

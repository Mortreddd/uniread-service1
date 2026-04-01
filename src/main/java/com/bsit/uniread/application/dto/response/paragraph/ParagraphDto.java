package com.bsit.uniread.application.dto.response.paragraph;

import com.bsit.uniread.domain.entities.paragraph.Paragraph;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ParagraphDto {

    private UUID id;
    private UUID chapterId;
    private String type;
    private String alignment;
    private Integer position;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;

}

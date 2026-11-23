package com.uniread.book.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ParagraphDto {

    private UUID id;

    private UUID chapterId;
    private String type;

    public String assetPublicId;

    private String alignment;

    private Integer position;

    private String content;

    private Instant createdAt;
    private Instant updatedAt;

}

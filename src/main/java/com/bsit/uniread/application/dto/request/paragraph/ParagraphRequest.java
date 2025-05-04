package com.bsit.uniread.application.dto.request.paragraph;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParagraphRequest {
    private String chapterId;
    private Integer position;
    private String alignment;
    private String type;
    private String content;
}

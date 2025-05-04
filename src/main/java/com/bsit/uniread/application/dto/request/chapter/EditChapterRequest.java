package com.bsit.uniread.application.dto.request.chapter;

import com.bsit.uniread.application.dto.request.paragraph.ParagraphRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditChapterRequest {
    private String title;
    private List<ParagraphRequest> paragraphs;
}

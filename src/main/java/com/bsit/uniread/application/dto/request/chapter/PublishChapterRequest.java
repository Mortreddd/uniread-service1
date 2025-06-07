package com.bsit.uniread.application.dto.request.chapter;

import com.bsit.uniread.domain.entities.chapter.ChapterStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublishChapterRequest {
    private ChapterStatus status;
}

package com.uniread.book.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.uniread.book.domain.entities.ChapterStatus;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChapterFilter {

    @Builder.Default
    private Integer pageNo = 0;
    @Builder.Default
    private Integer pageSize = 20;
    private String query;
    private ChapterStatus status;
    private Instant startDate;
    private Instant endDate;
    private Instant deletedAt;
}

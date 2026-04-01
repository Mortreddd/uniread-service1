package com.bsit.uniread.application.dto.request.chapter;

import com.bsit.uniread.domain.entities.chapter.ChapterStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
public class ChapterFilter {

    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private String query;
    private ChapterStatus status;
    private String sortBy = "asc";
    private String orderBy = "creaedAt";
    private Instant startDate;
    private Instant endDate;
    private Instant deletedAt;
}

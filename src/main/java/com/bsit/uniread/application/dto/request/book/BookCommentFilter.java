package com.bsit.uniread.application.dto.request.book;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
public class BookCommentFilter {
    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private String sortBy = "asc";
    private String orderBy = "createdAt";
    private Instant startDate;
    private Instant endDate;
}

package com.bsit.uniread.application.dto.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
public class UserFilter {
    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private String query;
    private String sortBy = "asc";
    private String orderBy = "createdAt";
    private Boolean emailVerified;
    private Instant startDate;
    private Instant endDate;
    private Instant bannedAt;
    private Instant deletedAt;
}

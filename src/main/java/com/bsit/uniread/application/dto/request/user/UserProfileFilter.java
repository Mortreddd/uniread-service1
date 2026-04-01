package com.bsit.uniread.application.dto.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
public class UserProfileFilter {
    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private String category = "ALL";
    private String query = "";
    private String sortBy = "asc";
    private String orderBy = "createdAt";
    private Instant deletedAt;
}

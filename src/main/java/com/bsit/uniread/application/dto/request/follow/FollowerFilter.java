package com.bsit.uniread.application.dto.request.follow;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FollowerFilter {
    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private String query;
}

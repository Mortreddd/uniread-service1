package com.uniread.social.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FollowingFilter {
    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private String query;
}

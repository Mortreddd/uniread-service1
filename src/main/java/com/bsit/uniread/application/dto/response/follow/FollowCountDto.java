package com.bsit.uniread.application.dto.response.follow;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowCountDto {
    private Long followersCount;
    private Long followingsCount;
}

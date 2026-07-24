package com.uniread.social.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowCountDto {
    private Long followersCount;
    private Long followingsCount;
}

package com.bsit.uniread.application.dto.response.follow;

import com.bsit.uniread.application.dto.response.user.AuthorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class FollowDto {
    private UUID id;
    private UUID followerId;
    private UUID followingId;
    private Instant createdAt;
}

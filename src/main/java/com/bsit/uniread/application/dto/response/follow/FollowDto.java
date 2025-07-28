package com.bsit.uniread.application.dto.response.follow;

import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FollowDto {
    private UUID id;
    private AuthorDto follower;
    private AuthorDto following;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FollowDto(Follow follow) {
        User follower = follow.getFollower();
        User following = follow.getFollowing();
        this.id = follow.getId();
        this.follower = new AuthorDto(follower);
        this.following = new AuthorDto(following);
        this.createdAt = follow.getCreatedAt();
        this.updatedAt = follow.getUpdatedAt();
    }

}

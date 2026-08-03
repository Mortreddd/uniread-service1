package com.uniread.user.dto.response;

import com.uniread.user.domain.entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserDetail {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private Gender gender;

    private String avatarUrl;

    private Long followersCount;
    private Long followingsCount;
    private Long publishedStoriesCount;
    private Boolean isFollowing;
    private Boolean isFollower;
    private Boolean isMutualFollowing;
}

package com.bsit.uniread.application.dto.response.follow;

import com.bsit.uniread.domain.entities.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class FollowUserDto {

    private UUID userId;
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private Gender gender;
    private String photoUrl;
    private Boolean isFollower;
    private Boolean isFollowing;
    private Boolean isMutualFollowing;
}

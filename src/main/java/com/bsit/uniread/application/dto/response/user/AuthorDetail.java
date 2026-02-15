package com.bsit.uniread.application.dto.response.user;

import com.bsit.uniread.domain.entities.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AuthorDetail {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private Gender gender;
    private String photoUrl;
    private Long followersCount;
    private Long followingsCount;
    private Long publishedStoriesCount;
    private Boolean isFollowing;
    private Boolean isFollower;
    private Boolean isMutualFollowing;
}

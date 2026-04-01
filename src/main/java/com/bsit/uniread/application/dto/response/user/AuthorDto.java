package com.bsit.uniread.application.dto.response.user;

import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthorDto {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private Gender gender;
    private String avatarUrl;
    private Long followersCount;
    private Long followingsCount;
    private Long storiesCount;
    private Boolean isFollowing;
    private Boolean isMutualFollowing;
}

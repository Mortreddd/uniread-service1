package com.uniread.user.dto.response;


import com.uniread.user.domain.entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorProfileDetail {
    private UUID id;
    private String username;

    private String firstName;
    private String lastName;
    private String displayName;
    private Gender gender;
    private String avatarUrl;
    private String coverUrl;
    private Integer followersCount;
    private Integer followingsCount;
    private Boolean isFollowing;
    private Boolean isFollower;
    private Boolean isMutualFollowing;
    private Integer booksCount;

}

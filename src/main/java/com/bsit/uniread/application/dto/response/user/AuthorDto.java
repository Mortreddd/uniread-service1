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
    private String photoUrl;
    private Long followersCount;
    private Long followingsCount;
    private Long storiesCount;
    private Boolean isFollowing;
    private Boolean isMutualFollowing;

    public AuthorDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.fullName = user.getFullName();
        this.gender = user.getGender();
        this.photoUrl = user.getPhotoUrl();
        this.followersCount = user.getFollowersCount();
        this.followingsCount = user.getFollowingsCount();
        this.storiesCount = user.getPublishedStoriesCount();
    }
}

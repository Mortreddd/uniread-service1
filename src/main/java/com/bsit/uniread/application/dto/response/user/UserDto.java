package com.bsit.uniread.application.dto.response.user;

import com.bsit.uniread.application.dto.response.follow.FollowDto;
import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String googleUuid;
    private String firstName;
    private String lastName;
    private String username;
    private Gender gender;
    private String email;
    private String photoUrl;
    private RoleDto role;
    private LocalDateTime emailVerifiedAt;
    private String fcmToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime bannedAt;
    private LocalDateTime deletedAt;
    private List<FollowDto> followers;
    private List<FollowDto> followings;
    private Boolean isEmailVerified;
    private Boolean isUser;
    private Boolean isSuperAdmin;
    private Boolean isAdmin;
    private Boolean isBanned;
    private String fullName;
    private Long followersCount;
    private Long followingsCount;
    private Long storiesCount;

    public UserDto (User user) {
        this.id = user.getId();
        this.googleUuid = user.getGoogleUuid();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.gender = user.getGender();
        this.email = user.getEmail();
        this.photoUrl = user.getPhotoUrl();
        this.role = new RoleDto(user.getRole());
        this.emailVerifiedAt = user.getEmailVerifiedAt();
        this.fcmToken = user.getFcmToken();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.deletedAt = user.getDeletedAt();
        this.followers = user.getFollowers().stream().map(FollowDto::new).toList();
        this.followings = user.getFollowings().stream().map(FollowDto::new).toList();
        this.isEmailVerified = user.getIsEmailVerified();
        this.isUser = user.getIsUser();
        this.isSuperAdmin = user.getIsSuperAdmin();
        this.isAdmin = user.getIsAdmin();
        this.isBanned = user.getIsBanned();
        this.fullName = user.getFullName();
        this.followersCount = user.getFollowersCount();
        this.followingsCount = user.getFollowingsCount();
        this.storiesCount = user.getStoriesCount();
    }
}
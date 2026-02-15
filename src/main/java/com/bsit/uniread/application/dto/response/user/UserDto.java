package com.bsit.uniread.application.dto.response.user;

import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String username;
    private Gender gender;
    private String email;
    private String photoUrl;
    private Role role;
    private LocalDateTime emailVerifiedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime bannedAt;
    private LocalDateTime unbannedAt;
    private LocalDateTime deletedAt;
    private Boolean isEmailVerified;
    private Boolean isUser;
    private Boolean isSuperAdmin;
    private Boolean isAdmin;
    private Boolean isBanned;

    public UserDto (User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.gender = user.getGender();
        this.email = user.getEmail();
        this.photoUrl = user.getPhotoUrl();
        this.role = user.getRole();
        this.emailVerifiedAt = user.getEmailVerifiedAt();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.deletedAt = user.getDeletedAt();
        this.unbannedAt = user.getUnbannedAt();
        this.isEmailVerified = user.getIsEmailVerified();
        this.isUser = user.getIsUser();
        this.isSuperAdmin = user.getIsSuperAdmin();
        this.isAdmin = user.getIsAdmin();
        this.isBanned = user.getIsBanned();
    }



}
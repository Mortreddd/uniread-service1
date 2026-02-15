package com.bsit.uniread.application.dto.response.user;

import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CurrentUser {
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

    public CurrentUser(CustomUserDetails userDetails)  {
        this.id = userDetails.getId();
        this.firstName = userDetails.getFirstName();
        this.lastName = userDetails.getLastName();
        this.fullName = userDetails.getFullName();
        this.username = userDetails.getUsername();
        this.gender = userDetails.getGender();
        this.email = userDetails.getEmail();
        this.photoUrl = userDetails.getPhotoUrl();
        this.role = userDetails.getRole();
        this.emailVerifiedAt = userDetails.getEmailVerifiedAt();
        this.createdAt = userDetails.getCreatedAt();
        this.updatedAt = userDetails.getUpdatedAt();
        this.deletedAt = userDetails.getDeletedAt();
        this.unbannedAt = userDetails.getUnbannedAt();
        this.isEmailVerified = userDetails.getIsEmailVerified();
        this.isUser = userDetails.getIsUser();
        this.isSuperAdmin = userDetails.getIsSuperAdmin();
        this.isAdmin = userDetails.getIsAdmin();
        this.isBanned = userDetails.getIsBanned();
    }
}

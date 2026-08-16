package com.uniread.user.dto.response;

import com.uniread.user.domain.entities.Gender;
import com.uniread.user.domain.entities.RoleType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CurrentUser {
    private UUID id;
    private String username;
    private String email;
    private Boolean emailVerified;
    private Boolean hasAdminAccess;
    private CurrentUserProfile profile;


    @Getter
    @Setter
    @Builder
    public static class CurrentUserProfile {
        private String displayName;
        private String firstName;
        private String lastName;
        private String fullName;
        private String avatarUrl;
        private String avatarPublicId;
        private Gender gender;
    }
}

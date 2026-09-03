package com.uniread.admin.dto.response;

import com.uniread.user.domain.entities.Gender;
import com.uniread.user.domain.entities.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserMonitoringDto {
    private UUID id;
    private UserProfileMonitor profile;
    private String username;
    private String email;
    private Instant emailVerifiedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant bannedAt;
    private Instant unbannedAt;
    private Instant deletedAt;
    private Boolean isEmailVerified;

    @Getter
    @Builder
    @Setter
    public static class UserProfileMonitor {
        private UUID id;
        private UUID userId;
        private String firstName;
        private String lastName;
        private String displayName;
        private Gender gender;
        private String bio;

        private String avatarUrl;
        private String avatarPublicId;

        private String coverUrl;
        private String coverPublicId;

    }
}

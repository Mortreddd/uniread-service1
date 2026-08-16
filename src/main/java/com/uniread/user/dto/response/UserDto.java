package com.uniread.user.dto.response;

import com.uniread.user.domain.entities.RoleType;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private ProfileDetailsDto profile;
    private String username;
    private String email;
    private RoleType role;
    private Instant emailVerifiedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant bannedAt;
    private Instant unbannedAt;
    private Instant deletedAt;
    private Boolean isEmailVerified;

}
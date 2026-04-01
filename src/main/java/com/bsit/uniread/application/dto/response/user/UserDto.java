package com.bsit.uniread.application.dto.response.user;

import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.Role;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String displayName;
    private Gender gender;
    private String email;
    private String avatarUrl;
    private Role role;
    private Instant emailVerifiedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant bannedAt;
    private Instant unbannedAt;
    private Instant deletedAt;
    private Boolean isEmailVerified;

}
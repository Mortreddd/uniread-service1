package com.uniread.user.dto.response;

import com.uniread.user.domain.entities.Gender;
import com.uniread.user.domain.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CurrentUser {
    private UUID id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String username;
    private Gender gender;
    private String email;
    private String avatarPhoto;
    private Role role;
    private Instant emailVerifiedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant bannedAt;
    private Instant unbannedAt;
    private Instant deletedAt;
}

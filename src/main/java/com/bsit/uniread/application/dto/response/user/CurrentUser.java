package com.bsit.uniread.application.dto.response.user;

import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.Role;
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
    private String photoUrl;
    private Role role;
    private Instant emailVerifiedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant bannedAt;
    private Instant unbannedAt;
    private Instant deletedAt;
}

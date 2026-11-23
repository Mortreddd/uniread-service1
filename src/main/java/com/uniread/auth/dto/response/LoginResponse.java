package com.uniread.auth.dto.response;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private Boolean success;
    private String message;
    private UUID id;
    private String username;
    private String displayName;
    private String email;
    private Instant emailVerifiedAt;
    private Instant bannedAt;
    private Instant unbannedAt;
    private Instant deletedAt;
    private Collection<? extends GrantedAuthority> authorities;
}
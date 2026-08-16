package com.uniread.auth.domain.entities;

import com.uniread.user.domain.entities.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UUID id;
    private final String email;
    private final String password;
    private final String username;

    private final Instant emailVerifiedAt;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant bannedAt;
    private final Instant unbannedAt;
    private final Instant deletedAt;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(
            UUID id,
            String email,
            String password,
            String username,
            Instant emailVerifiedAt,
            Instant createdAt,
            Instant updatedAt,
            Instant bannedAt,
            Instant unbannedAt,
            Instant deletedAt,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.emailVerifiedAt = emailVerifiedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.bannedAt = bannedAt;
        this.unbannedAt = unbannedAt;
        this.deletedAt = deletedAt;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return deletedAt == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return bannedAt == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return emailVerifiedAt != null
                && deletedAt == null
                && bannedAt == null;
    }
}
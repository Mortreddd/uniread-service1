package com.bsit.uniread.domain.entities.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UUID id;
    private final String email;
    private final String password;
    private final Role role;
    private final String firstName;
    private final String lastName;
    private final String fullName;
    private final String username;
    private final Gender gender;
    private final String photoUrl;
    private final LocalDateTime emailVerifiedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime bannedAt;
    private final LocalDateTime unbannedAt;
    private final LocalDateTime deletedAt;
    private final Boolean isEmailVerified;
    private final Boolean isUser;
    private final Boolean isBanned;
    private final Boolean isAdmin;
    private final Boolean isSuperAdmin;


    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.gender = user.getGender();
        this.photoUrl = user.getPhotoUrl();
        this.emailVerifiedAt = user.getEmailVerifiedAt();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.deletedAt = user.getDeletedAt();
        this.bannedAt = user.getBannedAt();
        this.unbannedAt = user.getUnbannedAt();
        this.isEmailVerified = user.getIsEmailVerified();
        this.isUser = user.getIsUser();
        this.isBanned = user.getIsBanned();
        this.isAdmin = user.getIsAdmin();
        this.isSuperAdmin = user.getIsSuperAdmin();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

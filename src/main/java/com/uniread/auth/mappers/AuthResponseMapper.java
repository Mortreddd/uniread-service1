package com.uniread.auth.mappers;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.domain.entities.User;
import com.uniread.auth.dto.response.LoginResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class AuthResponseMapper {


    /**
     * For newly logged in from oath provider
     * @param user
     * @return LoginResponse
     */
    public LoginResponse toLoginResponse(User user) {
        // Empty roles and permission for new user
        Collection<? extends GrantedAuthority> authorities = List.of();
        return LoginResponse.builder()
                .success(true)
                .message("Successfully logged in")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .emailVerifiedAt(user.getEmailVerifiedAt())
                .bannedAt(user.getBannedAt())
                .unbannedAt(user.getUnbannedAt())
                .deletedAt(user.getDeletedAt())
                .authorities(authorities)
                .build();
    }
    public LoginResponse toLoginResponse(CustomUserDetails user) {
        return LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .username(user.getUsername())
                .id(user.getId())
                .email(user.getEmail())
                .emailVerifiedAt(user.getEmailVerifiedAt())
                .bannedAt(user.getBannedAt())
                .unbannedAt(user.getUnbannedAt())
                .deletedAt(user.getDeletedAt())
                .authorities(user.getAuthorities())
                .build();
    }
}

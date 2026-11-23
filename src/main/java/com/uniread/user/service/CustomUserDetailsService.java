package com.uniread.user.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.domain.entities.Permission;
import com.uniread.auth.domain.entities.Role;
import com.uniread.auth.domain.entities.User;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        var user = userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> {
                    log.warn("User not found with username/email: {}", usernameOrEmail);
                    return new UsernameNotFoundException("Credentials do not match our records");
                });


        return toUserDetails(user);

    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return toUserDetails(user);
    }

    public UserDetails loadUserById(UUID userId) throws UsernameNotFoundException {
        var user = userRepository.findCurrentUserDetailsById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return toUserDetails(user);
    }

    private CustomUserDetails toUserDetails(User user) {

        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Role role : user.getRoles()) {

            authorities.add(
                    new SimpleGrantedAuthority(
                            "ROLE_" + role.getCode()
                    )
            );

            for (Permission permission : role.getPermissions()) {

                authorities.add(
                        new SimpleGrantedAuthority(
                                permission.getCode()
                        )
                );
            }
        }

        return CustomUserDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .displayName(user.getProfile() != null ? user.getProfile().getDisplayName() : "Empty Profile")
                .password(user.getPassword())
                .emailVerifiedAt(user.getEmailVerifiedAt())
                .bannedAt(user.getBannedAt())
                .unbannedAt(user.getUnbannedAt())
                .deletedAt(user.getDeletedAt())
                .authorities(authorities)
                .build();
    }

}

package com.uniread.auth.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.domain.events.GoogleRegistrationEvent;
import com.uniread.auth.dto.response.GoogleUserInfoResponse;
import com.uniread.auth.dto.response.LoginResponse;
import com.uniread.auth.mappers.AuthResponseMapper;
import com.uniread.user.service.CustomUserDetailsService;
import com.uniread.user.service.UserService;
import com.uniread.auth.domain.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService implements OAuthService {

    @Value("${client.url}")
    private String clientUrl;
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final ApplicationEventPublisher publisher;
    private final TokenValidationService tokenValidationService;
    private final AuthResponseMapper mapper;

    @Transactional
    @Override
    public LoginResponse handleOAuthLogin(String accessToken) {
        var userInfo = tokenValidationService.validateGoogleToken(accessToken);
        validateGoogleUserInfo(userInfo);

        var existingUser = (CustomUserDetails) userDetailsService.loadUserByEmail(userInfo.getEmail());
        log.info("Generating google token for user: {}", userInfo.getEmail());
        if(existingUser != null) {
            return mapper.toLoginResponse(existingUser);
        } else {
            var user = userService.createGoogleUser(userInfo);
            publisher.publishEvent(new GoogleRegistrationEvent(this, user, userInfo));

            return mapper.toLoginResponse(user);
        }


    }
    private void validateGoogleUserInfo(GoogleUserInfoResponse userInfo) {
        if (userInfo == null) {
            throw new IllegalArgumentException("Google user information cannot be null");
        }
        if (userInfo.getEmail() == null || userInfo.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required for Google user registration");
        }
        if (userInfo.getSub() == null || userInfo.getSub().isBlank()) {
            throw new IllegalArgumentException("Google user ID (sub) is required");
        }
    }


}
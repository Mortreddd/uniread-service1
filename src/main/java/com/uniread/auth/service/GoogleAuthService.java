package com.uniread.auth.service;

import com.uniread.auth.domain.events.GoogleRegistrationEvent;
import com.uniread.auth.dto.response.GoogleUserInfoResponse;
import com.uniread.auth.dto.response.LoginResponse;
import com.uniread.user.service.UserService;
import com.uniread.user.domain.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService implements OAuthService {

    @Value("${client.url}")
    private String clientUrl;

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final JsonWebTokenService jsonWebTokenService;
    private final TokenValidationService tokenValidationService;

    @Override
    public LoginResponse handleOAuthLogin(String accessToken) {
        var userInfo = tokenValidationService.validateGoogleToken(accessToken);
        validateGoogleUserInfo(userInfo);

        var existingUser = userService.getUserByEmail(userInfo.getEmail());

        User user;
        if(existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = userService.createGoogleUser(userInfo);
            publisher.publishEvent(new GoogleRegistrationEvent(this, user, userInfo));
        }

        log.info("Generated google token for user: {}", user.getEmail());

        return buildLoginResponse(user);
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

    private LoginResponse buildLoginResponse(User user) {
        return LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .user(LoginResponse.UserInfo.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole() != null ? user.getRole().name() : "USER")
                        .emailVerified(user.getIsEmailVerified())
                        .build())
                .build();
    }
}
package com.bsit.uniread.application.services.auth;

import com.bsit.uniread.application.dto.response.auth.GoogleUserInfoResponse;
import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.services.user.UserProfileService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService implements OAuthService {

    @Value("${client.url}")
    private String clientUrl;

    private final UserService userService;
    private final JsonWebTokenService jsonWebTokenService;
    private final UserProfileService userProfileService;
    private final TokenValidationService tokenValidationService;

    @Override
    public LoginResponse handleOAuthLogin(String accessToken) {
        GoogleUserInfoResponse userInfo = tokenValidationService.validateGoogleToken(accessToken);

        User user = findOrCreateUser(userInfo);

        String accessTokenJwt = jsonWebTokenService.generateAccessToken(user.getId(), userInfo.getEmail());
        String refreshToken = jsonWebTokenService.generateRefreshToken(user.getId(), userInfo.getEmail());

        log.debug("Generated tokens for user: {}", user.getEmail());

        return buildLoginResponse(accessTokenJwt, refreshToken);
    }

    private void validateUserInfo(GoogleUserInfoResponse userInfo) {
        if (userInfo == null || userInfo.getEmail() == null) {
            throw new IllegalArgumentException("Invalid Google user information");
        }
    }

    private User findOrCreateUser(GoogleUserInfoResponse userInfo) {
        return userService.getUserByEmail(userInfo.getEmail())
                .orElseGet(() -> createNewUser(userInfo));
    }

    private User createNewUser(GoogleUserInfoResponse userInfo) {
        User user = userService.createGoogleUser(userInfo);
        userProfileService.createUserProfile(user, userInfo);
        log.info("Created new user from Google OAuth: {}", user.getEmail());
        return user;
    }

    private LoginResponse buildLoginResponse(String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .iat(System.currentTimeMillis())
                .iss(clientUrl)
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }
}
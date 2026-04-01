package com.bsit.uniread.application.services.auth;

import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.dto.response.user.UserDto;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.infrastructure.handler.exceptions.auth.InvalidCredentialsException;
import com.bsit.uniread.infrastructure.handler.exceptions.auth.InvalidTokenException;
import com.bsit.uniread.infrastructure.handler.exceptions.auth.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JsonWebTokenService jsonWebTokenService;

    @Value("${client.url}")
    private String clientUrl;

    @Transactional(readOnly = true)
    public LoginResponse loginUser(String email, String password) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            var userDetails = (CustomUserDetails) authentication.getPrincipal();
            var user = userService.getUserById(userDetails.getId());

            String accessToken = jsonWebTokenService.generateAccessToken(user.getId(), user.getEmail());
            String refreshToken = jsonWebTokenService.generateRefreshToken(user.getId(), user.getEmail());

            log.info("User logged in successfully: {}", email);

            return buildLoginResponse(user, accessToken, refreshToken);

        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for email: {}", email);
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    @Transactional(readOnly = true)
    public LoginResponse refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new InvalidTokenException("Refresh token is required");
        }

        if (jsonWebTokenService.isTokenExpired(refreshToken)) {
            log.warn("Expired refresh token used");
            throw new TokenExpiredException("Session expired. Please log in again.");
        }

        try {
            var userId = jsonWebTokenService.extractUserId(refreshToken);
            UserDto user = userService.getUserById(userId);

            String newAccessToken = jsonWebTokenService.generateAccessToken(user.getId(), user.getEmail());
            String newRefreshToken = jsonWebTokenService.generateRefreshToken(user.getId(), user.getEmail());

            log.debug("Token refreshed successfully for user: {}", user.getEmail());

            return buildLoginResponse(user, newAccessToken, newRefreshToken);

        } catch (Exception e) {
            log.error("Error refreshing token", e);
            throw new InvalidTokenException("Invalid refresh token");
        }
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        // Implementation for password reset
        // Generate token, save to database, send email
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        // Implementation for password reset
        // Validate token, update password
    }

    private LoginResponse buildLoginResponse(UserDto user, String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .iat(System.currentTimeMillis())
                .iss(clientUrl)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
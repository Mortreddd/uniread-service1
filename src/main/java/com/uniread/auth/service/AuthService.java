package com.uniread.auth.service;

import com.uniread.auth.domain.entities.VerificationToken;
import com.uniread.auth.domain.events.NewVerifiedUserEvent;
import com.uniread.auth.domain.events.UserRegisteredEvent;
import com.uniread.auth.dto.request.UserRegistrationRequest;
import com.uniread.auth.dto.response.LoginResponse;
import com.uniread.auth.repositories.VerificationTokenRepository;
import com.uniread.common.dto.api.SuccessResponse;
import com.uniread.common.exceptions.DuplicateResourceException;
import com.uniread.common.exceptions.ValidationException;
import com.uniread.common.utils.DateUtil;
import com.uniread.user.dto.response.UserDto;
import com.uniread.user.service.UserService;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.exceptions.InvalidCredentialsException;
import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.auth.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final VerificationTokenRepository verificationRepository;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JsonWebTokenService jsonWebTokenService;
    private final ApplicationEventPublisher publisher;

    @Value("${client.url}")
    private String clientUrl;

    @Transactional(readOnly = true)
    public LoginResponse loginUser(String email, String password) {
        try {
            log.info("Login attempt for email: {}", email);

            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            var userDetails = (CustomUserDetails) authentication.getPrincipal();
            if(userDetails == null) throw new InvalidCredentialsException("Unable to process login credentials");

            var user = userService.getUserById(userDetails.getId());

            log.info("User logged in : {}", email);

            return buildLoginResponse(user);

        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            log.warn("Failed login attempt for email: {}", email);
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    @Transactional
    public SuccessResponse registerUser(UserRegistrationRequest request) {

        validateUserRegistration(request);
        try {

            var user = userService.createUser(request);
            publisher.publishEvent(new UserRegisteredEvent(user.getId(), request));

            log.info("User registered successfully: {}", request.getEmail());

            return SuccessResponse.builder()
                    .code(HttpStatus.OK.value())
                    .message("Successfully registered")
                    .build();
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate user registration attempt: {}", request.getEmail());

            throw new ValidationException("Email or username already exists");
        }
    }

    @Transactional
    public SuccessResponse confirmEmail(String token) {
        var vt = verificationRepository.findById(token)
                .orElseThrow(() -> new ValidationException("Invalid token"));

        if (vt.isUsed()) {
            throw new ValidationException("Token already used");
        }

        if (vt.isExpired()) {
            throw new ValidationException("Token expired");
        }

        var user = userService.getUserById(vt.getUserId());
        userService.markEmailVerified(vt.getUserId());
        vt.markAsUsed();

        verificationRepository.save(vt);

        var event = new NewVerifiedUserEvent(
                vt.getUserId(),
                user.getEmail(),
                user.getUsername()
        );

        publisher.publishEvent(event);

        return SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully verified email")
                .build();
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

            log.debug("Token refreshed successfully for user: {}", user.getEmail());

            return buildLoginResponse(user);

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

    private LoginResponse buildLoginResponse(UserDto user) {
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

    private void validateUserRegistration(UserRegistrationRequest request) {
        if(request == null) throw new IllegalArgumentException("Unable to process this request");

        if(!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("Password and Confirm Password do not match");
        }

        if(userService.isUsernameExists(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

    }
}
package com.bsit.uniread.application.services.auth;

import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.auth.UserRegistrationRequest;
import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.services.otp.OtpService;
import com.bsit.uniread.domain.entities.auth.Otp;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.infrastructure.handler.exceptions.auth.InvalidCredentialsException;
import com.bsit.uniread.infrastructure.handler.exceptions.auth.TokenExpiredException;
import com.bsit.uniread.infrastructure.handler.publishers.auth.UserRegistrationPublisher;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRegistrationPublisher userRegistrationPublisher;
    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final JsonWebTokenService jsonWebTokenService;

    @Value("${client.url}")
    private String clientUrl;

    /**
     * Authenticate the user using email and password
     * @param email
     * @param password
     * @return loginResponse
     */
    public LoginResponse loginUser(String email, String password) {
        log.info("Email received {}", email);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        if(!authentication.isAuthenticated()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = jsonWebTokenService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jsonWebTokenService.generateRefreshToken(user.getId(), user.getEmail());

        return LoginResponse
                .builder()
                .iat(System.currentTimeMillis())
                .iss(clientUrl)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public LoginResponse refreshToken(String refreshToken) {
        if(jsonWebTokenService.isTokenExpired(refreshToken)) {
            throw new TokenExpiredException("Session is expired. Please log in.");
        }

        User user = userService.getUserById(jsonWebTokenService.extractUserId(refreshToken));
        String newAccessToken = jsonWebTokenService.generateAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jsonWebTokenService.generateRefreshToken(user.getId(), user.getEmail());

        return LoginResponse
                .builder()
                .iat(System.currentTimeMillis())
                .iss(clientUrl)
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    /**
     * Accepts a UserRegistrationRequest object for handling user registration
     * Additionally, it publishes the userRegistrationEvent
     * @param userRegistrationRequest
     */
    public void registerUser(UserRegistrationRequest userRegistrationRequest) {
        User newUser = userService.save(
                User.builder()
                    .firstName(userRegistrationRequest.getFirstName())
                    .lastName(userRegistrationRequest.getLastName())
                    .username(userRegistrationRequest.getUsername())
                    .email(userRegistrationRequest.getEmail())
                    .gender(userRegistrationRequest.getGender())
                    .role(Role.USER)
                    .password(new BCryptPasswordEncoder().encode(userRegistrationRequest.getPassword()))
                    .emailVerifiedAt(DateUtil.now())
                    .build()
        );

        userService.save(newUser);
    }

    /**
     * Sends an email confirmation for the requested forgot passwords
     * @param email
     * @throws MessagingException
     */
    public void sendForgotPassword(String email) throws MessagingException {
        Otp otp = otpService.generateOtp(email);
        emailService.sendForgotPasswordEmail(email, otp.getId());
    }

    /**
     * Verify the email of user
     * @param otpUuid
     * @return User
     */
    public void verifyEmailByOtpId(UUID otpUuid) {
        Otp otp = otpService.getOtpById(otpUuid);
        User user = userService.getUserByEmailOrThrow(otp.getEmail());

        userService.verifyUserEmail(user);
    }

    /**
     * Update username of the user
     * @param userId
     * @param username
     * @return SuccessResponse
     */
    public SuccessResponse setupUsername(UUID userId, String username) {
        userService.updateUsername(userId, username);
        return SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Username updated")
                .build();
    }

}

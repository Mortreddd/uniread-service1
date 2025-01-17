package com.bsit.uniread.application.services.auth;

import com.bsit.uniread.application.dto.request.auth.UserRegistrationRequest;
import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.services.otp.OtpService;
import com.bsit.uniread.application.services.role.RoleService;
import com.bsit.uniread.domain.entities.auth.Otp;
import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.RoleName;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.infrastructure.handler.exceptions.auth.InvalidCredentialsException;
import com.bsit.uniread.infrastructure.handler.publishers.auth.UserRegistrationPublisher;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRegistrationPublisher userRegistrationPublisher;
    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final JsonWebTokenService jsonWebTokenService;
    private final RoleService roleService;

    @Value("${client.url}")
    private String clientUrl;

    public LoginResponse loginUser(final String email, final String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        if(!authentication.isAuthenticated()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        String authToken = jsonWebTokenService.generateToken(email);

        return LoginResponse
                .builder()
                .iat(System.currentTimeMillis())
                .iss(clientUrl)
                .exp(new Date(System.currentTimeMillis() + DateUtil.JSON_WEB_TOKEN_EXPIRATION).getTime())
                .accessToken(authToken)
                .build();
    }

    /**
     * Accepts a UserRegistrationRequest object for handling user registration
     * Additionally, it publishes the userRegistrationEvent
     * @param userRegistrationRequest
     */
    public void registerUser(UserRegistrationRequest userRegistrationRequest) {
        Role userRole = roleService.getUserRole();
        User newUser = userService.saveUser(
                User.builder()
                    .firstName(userRegistrationRequest.getFirstName())
                    .lastName(userRegistrationRequest.getLastName())
                    .username(userRegistrationRequest.getUsername())
                    .email(userRegistrationRequest.getEmail())
                    .gender(userRegistrationRequest.getGender())
                    .role(userRole)
                    .password(new BCryptPasswordEncoder().encode(userRegistrationRequest.getPassword()))
                    .emailVerifiedAt(DateUtil.now())
                    .build()
        );

        // Disable after passing tests cases
        // userRegistrationPublisher.publishUserRegistration(newUser);
        userService.saveUser(newUser);
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
        User user = userService.getUserByEmail(otp.getEmail());

        userService.verifyUserEmail(user);
    }

}

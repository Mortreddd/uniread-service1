package com.bsit.uniread.application.services.auth;

import com.bsit.uniread.application.dto.response.auth.GoogleUserInfoResponse;
import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.publishers.auth.UserRegistrationPublisher;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    @Value("${client.url}")
    private String clientUrl;

    private final UserRegistrationPublisher userRegistrationPublisher;
    private final UserService userService;
    private final JsonWebTokenService jsonWebTokenService;
    private final BCryptPasswordEncoder encoder;

    public LoginResponse googleLogin(GoogleUserInfoResponse userInfo) {

        Optional<User> optionalUser = userService.getUserByEmail(userInfo.getEmail());
        User user = optionalUser.orElseGet(() -> userService.save(User.builder()
                .firstName(userInfo.getGivenName())
                .lastName(userInfo.getFamilyName())
                .email(userInfo.getEmail())
                .username(null)
                .password(encoder.encode(StringUtils.randomAlphanumeric(16)))
                .emailVerifiedAt(DateUtil.now())
                .gender(Gender.OTHER)
                .googleUuid(userInfo.getSub())
                .photoUrl(userInfo.getPicture())
                .role(Role.USER)
                .build()
        ));

        String accessToken = jsonWebTokenService.generateAccessToken(user.getId(), userInfo.getEmail());
        String refreshToken = jsonWebTokenService.generateRefreshToken(user.getId(), userInfo.getEmail());
        return LoginResponse
                .builder()
                .iat(System.currentTimeMillis())
                .iss(clientUrl)
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }
}

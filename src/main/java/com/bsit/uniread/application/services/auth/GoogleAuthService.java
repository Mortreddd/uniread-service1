package com.bsit.uniread.application.services.auth;

import com.bsit.uniread.application.dto.response.auth.GoogleUserInfoResponse;
import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.infrastructure.handler.exceptions.auth.InvalidCredentialsException;
import com.bsit.uniread.infrastructure.handler.publishers.auth.UserRegistrationPublisher;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    @Value("${client.url}")
    private String clientUrl;

    private final UserRegistrationPublisher userRegistrationPublisher;
    private final UserService userService;
    private final JsonWebTokenService jsonWebTokenService;

    public LoginResponse googleLogin(GoogleUserInfoResponse userInfo) {

        if(userService.emailNotExists(userInfo.getEmail())) {
            userRegistrationPublisher.publishOAuthRegistration(userInfo);
        }

        String authToken = jsonWebTokenService.generateToken(userInfo.getEmail());
        return LoginResponse
                .builder()
                .iat(System.currentTimeMillis())
                .iss(clientUrl)
                .exp(new Date(System.currentTimeMillis() + DateUtil.JSON_WEB_TOKEN_EXPIRATION).getTime())
                .accessToken(authToken)
                .build();
    }
}

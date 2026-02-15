package com.bsit.uniread.application.controllers.auth;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.auth.GoogleOAuthRequest;
import com.bsit.uniread.application.dto.response.auth.GoogleUserInfoResponse;
import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.dto.response.user.SimpleUserInfo;
import com.bsit.uniread.application.services.auth.GoogleAuthService;
import com.bsit.uniread.application.services.auth.JsonWebTokenService;
import com.bsit.uniread.application.services.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiEndpoints.AUTH)
public class GoogleOAuthController {

    @Value("${spring.security.oauth2.client.registration.provider.google.user-info-uri}")
    private String googleUserInfoUri;

    private final UserService userService;
    private final JsonWebTokenService jsonWebTokenService;
    private final GoogleAuthService googleAuthService;
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping(path = "/google")
    public ResponseEntity<SimpleUserInfo> googleAuthentication(
            @RequestBody GoogleOAuthRequest request,
            HttpServletResponse httpServletResponse
    ) {
        String accessToken = request.getAccessToken();
        String requestUri = String.format("%s?access_token=%s", googleUserInfoUri, accessToken);
        ResponseEntity<GoogleUserInfoResponse> response = restTemplate.exchange(
                requestUri,
                HttpMethod.GET,
                null,
                GoogleUserInfoResponse.class
        );

        LoginResponse loginResponse = googleAuthService.googleLogin(response.getBody());
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", loginResponse.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", loginResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/auth/refresh-token")
                .maxAge(Duration.ofDays(7))
                .build();


        SimpleUserInfo userInfo = new SimpleUserInfo(userService.getUserById(jsonWebTokenService.extractUserId(loginResponse.getAccessToken())));
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return ResponseEntity.ok()
                .body(userInfo);
    }
}

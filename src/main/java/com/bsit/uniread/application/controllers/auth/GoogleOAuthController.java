package com.bsit.uniread.application.controllers.auth;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.auth.GoogleOAuthRequest;
import com.bsit.uniread.application.dto.response.auth.GoogleUserInfoResponse;
import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.services.auth.GoogleAuthService;
import com.bsit.uniread.application.services.auth.JsonWebTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiEndpoints.AUTH)
public class GoogleOAuthController {

    @Value("${spring.security.oauth2.client.registration.provider.google.user-info-uri}")
    private String googleUserInfoUri;

    private final GoogleAuthService googleAuthService;
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping(path = "/google")
    public ResponseEntity<LoginResponse> googleAuthentication(@RequestBody GoogleOAuthRequest request) {
        String accessToken = request.getAccessToken();
        String requestUri = String.format("%s?access_token=%s", googleUserInfoUri, accessToken);
        ResponseEntity<GoogleUserInfoResponse> response = restTemplate.exchange(
                requestUri,
                HttpMethod.GET,
                null,
                GoogleUserInfoResponse.class
        );

        return ResponseEntity.ok()
                .body(googleAuthService.googleLogin(response.getBody()));
    }
}

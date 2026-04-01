package com.bsit.uniread.application.controllers.auth;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.auth.GoogleOAuthRequest;
import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.services.auth.GoogleAuthService;
import com.bsit.uniread.application.services.auth.OAuthCookieService;
import com.bsit.uniread.application.services.auth.TokenValidationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiEndpoints.AUTH)
@Slf4j
public class GoogleOAuthController {

    private final GoogleAuthService googleAuthService;
    private final TokenValidationService tokenValidationService;
    private final OAuthCookieService cookieService;

    @PostMapping("/google")
    public ResponseEntity googleAuthentication(
            @Valid @RequestBody GoogleOAuthRequest request,
            HttpServletResponse httpServletResponse
    ) {
        log.debug("Processing Google OAuth authentication request");

        tokenValidationService.validateGoogleToken(request.getAccessToken());
        LoginResponse loginResponse = googleAuthService.handleOAuthLogin(request.getAccessToken());
        cookieService.setAuthCookies(httpServletResponse, loginResponse);

        return ResponseEntity.ok().build();
    }
}
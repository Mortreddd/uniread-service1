package com.uniread.auth.controllers;

import com.uniread.auth.dto.request.GoogleOAuthRequest;
import com.uniread.auth.dto.response.LoginResponse;
import com.uniread.auth.service.GoogleAuthService;
import com.uniread.auth.service.OAuthCookieService;
import com.uniread.auth.service.TokenValidationService;
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
@RequestMapping(path = "/auth")
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

        tokenValidationService.validateGoogleToken(request.getAccessToken());
        LoginResponse loginResponse = googleAuthService.handleOAuthLogin(request.getAccessToken());
        cookieService.setAuthCookies(httpServletResponse, loginResponse);

        return ResponseEntity.ok().build();
    }
}
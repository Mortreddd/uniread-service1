package com.bsit.uniread.application.controllers.auth;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.auth.ForgotPasswordRequest;
import com.bsit.uniread.application.dto.request.auth.LoginRequest;
import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.services.auth.AuthCookieService;
import com.bsit.uniread.application.services.auth.AuthService;
import com.bsit.uniread.infrastructure.handler.exceptions.auth.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = ApiEndpoints.AUTH)
public class AuthController {

    private final AuthService authService;
    private final AuthCookieService cookieService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse httpServletResponse
    ) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());

        LoginResponse response = authService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        cookieService.setAuthCookies(httpServletResponse, response.getAccessToken(), response.getRefreshToken());

        log.info("User logged in successfully: {}", loginRequest.getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        log.debug("Token refresh request received");

        String refreshToken = cookieService.extractRefreshToken(httpServletRequest)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found"));

        LoginResponse response = authService.refreshToken(refreshToken);
        cookieService.setAuthCookies(httpServletResponse, response.getAccessToken(), response.getRefreshToken());

        log.debug("Token refreshed successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse httpServletResponse) {
        log.info("Logout request received");
        cookieService.clearAuthCookies(httpServletResponse);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Password reset requested for email: {}", request.getEmail());
        authService.initiatePasswordReset(request.getEmail());
        return ResponseEntity.ok().build();
    }
}
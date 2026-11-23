package com.uniread.auth.controllers;

import com.uniread.auth.dto.request.*;
import com.uniread.auth.dto.response.LoginResponse;
import com.uniread.auth.service.AuthCookieService;
import com.uniread.auth.service.AuthService;
import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.common.dto.api.SuccessResponse;
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
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthCookieService cookieService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse httpServletResponse
    ) {

        var response = authService.loginUser(loginRequest);
        cookieService.setAuthCookies(httpServletResponse, response);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-account")
    public ResponseEntity verifyAccount(
            @Valid @RequestBody VerifyAccountRequest request
    ) {

        var response = authService.verifyAccount(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/verify-email")
    public ResponseEntity confirmEmail(
            @Valid @RequestBody VerifyEmailRequest request,
            HttpServletResponse httpServletResponse
    ) {
        var response = authService.confirmEmail(request.getToken());
        cookieService.setAuthCookies(httpServletResponse, response);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity register(
            @Valid @RequestBody UserRegistrationRequest request
    ) {
        var response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        String refreshToken = cookieService.extractRefreshToken(httpServletRequest)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found"));
        var response = authService.refreshToken(refreshToken);
        cookieService.setAuthCookies(httpServletResponse, response);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse httpServletResponse) {
        cookieService.clearAuthCookies(httpServletResponse);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<SuccessResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Password reset requested for username: {}", request.getUsername());
        var response = authService.initiatePasswordReset(request.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<SuccessResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        var response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}
package com.bsit.uniread.application.controllers.auth;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.auth.ForgotPasswordRequest;
import com.bsit.uniread.application.dto.request.auth.LoginRequest;
import com.bsit.uniread.application.dto.request.auth.RefreshTokenRequest;
import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.services.auth.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Api Endpoint - /api/v1/auth
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = ApiEndpoints.AUTH)
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticate user and accepts a LoginRequest object for logging in
     * @param loginRequest
     * @return LoginResponse
     */
    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> verifyLogin(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        LoginResponse response = authService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok().body(response);
    }

    /**
     * Sends a forgot password based on user's email
     * @param forgotPasswordRequest
     * @return ResponseEntity
     */
    @PostMapping(path = "/forgot-password", consumes = {"application/json"})
    public ResponseEntity<String> sendForgotPassword(
            @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest
    ) throws MessagingException {
        authService.sendForgotPassword(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok().build();

    }

    @PostMapping(path = "/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        LoginResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok()
                .body(response);
    }

    /**
     * Verify the url was sent to the user's email
     * @param id
     * @return ResponseEntity
     */
    @GetMapping(path = "/forgot-password")
    public ResponseEntity<String> verifyForgotPassword(
            @RequestParam("id") UUID id
    ) {
        authService.verifyEmailByOtpId(id);
        return ResponseEntity.ok().build();
    }
}

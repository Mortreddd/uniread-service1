package com.bsit.uniread.application.controllers.auth;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.auth.ForgotPasswordRequest;
import com.bsit.uniread.application.dto.request.auth.LoginRequest;
import com.bsit.uniread.application.dto.request.auth.RefreshTokenRequest;
import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.services.auth.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

/**
 * Api Endpoint - /api/v1/auth
 */
@Slf4j
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
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse httpServletResponse
    ) {
        LoginResponse response = authService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", response.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", response.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return ResponseEntity.ok().build();
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
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            clearAuthCookies(httpServletResponse);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build();
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refresh_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        LoginResponse response = authService.refreshToken(refreshToken);
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", response.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", response.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok()
                .build();
    }

    /**
     * Verify the url was sent to the user's email
     * @param id
     * @return ResponseEntity
     */
    @GetMapping(path = "/forgot-password")
    public ResponseEntity verifyForgotPassword(
            @RequestParam("id") UUID id
    ) {
        authService.verifyEmailByOtpId(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/logout")
    public ResponseEntity logout(
            HttpServletResponse httpServletResponse
    ) {
        clearAuthCookies(httpServletResponse);
        return ResponseEntity.ok().build();
    }

    private void clearAuthCookies(HttpServletResponse response) {
        ResponseCookie c1 = ResponseCookie.from("access_token", "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .build();
        ResponseCookie c2 = ResponseCookie.from("refresh_token", "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, c1.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, c2.toString());
    }
}

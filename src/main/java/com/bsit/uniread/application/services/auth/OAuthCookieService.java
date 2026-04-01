package com.bsit.uniread.application.services.auth;

import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.infrastructure.configurations.properties.CookieConfigProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthCookieService {

    private final CookieConfigProperties cookieConfig;

    public void setAuthCookies(HttpServletResponse response, LoginResponse loginResponse) {
        ResponseCookie accessTokenCookie = createAccessTokenCookie(loginResponse.getAccessToken());
        ResponseCookie refreshTokenCookie = createRefreshTokenCookie(loginResponse.getRefreshToken());
        
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        
        log.debug("Auth cookies set successfully");
    }

    public void clearAuthCookies(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
                
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/auth/refresh-token")
                .maxAge(0)
                .build();
                
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        
        log.debug("Auth cookies cleared");
    }

    private ResponseCookie createAccessTokenCookie(String token) {
        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(cookieConfig.isSecure())
                .sameSite(cookieConfig.getSameSite())
                .path("/")
                .maxAge(Duration.ofMinutes(cookieConfig.getAccessTokenMaxAgeMinutes()))
                .build();
    }

    private ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(cookieConfig.isSecure())
                .sameSite(cookieConfig.getSameSite())
                .path("/auth/refresh-token")
                .maxAge(Duration.ofDays(cookieConfig.getRefreshTokenMaxAgeDays()))
                .build();
    }
}
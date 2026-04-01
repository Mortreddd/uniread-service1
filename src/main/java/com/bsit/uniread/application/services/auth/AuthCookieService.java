package com.bsit.uniread.application.services.auth;

import com.bsit.uniread.infrastructure.configurations.properties.CookieConfigProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthCookieService {

    private final CookieConfigProperties cookieConfig;

    public void setAuthCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie accessTokenCookie = createAccessTokenCookie(accessToken);
        ResponseCookie refreshTokenCookie = createRefreshTokenCookie(refreshToken);
        
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        
        log.debug("Auth cookies set successfully");
    }

    public void clearAuthCookies(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(cookieConfig.isSecure())
                .sameSite(cookieConfig.getSameSite())
                .build();
                
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", "")
                .maxAge(0)
                .path("/auth/refresh-token")
                .httpOnly(true)
                .secure(cookieConfig.isSecure())
                .sameSite(cookieConfig.getSameSite())
                .build();
                
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        
        log.debug("Auth cookies cleared");
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
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
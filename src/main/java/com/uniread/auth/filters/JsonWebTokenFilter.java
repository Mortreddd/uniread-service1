package com.uniread.auth.filters;

import com.uniread.auth.service.JsonWebTokenService;
import com.uniread.user.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonWebTokenFilter extends OncePerRequestFilter {

    private final JsonWebTokenService jsonWebTokenService;
    private final CustomUserDetailsService customUserDetailsService;

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/auth/refresh-token",
            "/auth/login",
            "/auth/register"
    );

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {


        // Check if this is a refresh token request
        if (isRefreshPath(request)) {
            log.info("Skipping JWT filter for refresh token request");
            filterChain.doFilter(request, response);
            return;
        }

        // Log all cookies
        Cookie[] cookies = request.getCookies();

        String accessToken = getTokenFromCookies(cookies);

        if (accessToken == null) {
            log.warn("No access token found in cookies");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            UUID userId = jsonWebTokenService.extractUserId(accessToken);

            if (SecurityContextHolder.getContext().getAuthentication() == null
                    && jsonWebTokenService.validateToken(accessToken, userId)) {

                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                log.info("Loaded user details for user: {}", userDetails.getUsername());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Authentication set successfully for user: {}", userId);
            } else {
                log.warn("Authentication not set - either already authenticated or token validation failed");
            }
        } catch (ExpiredJwtException e) {
            log.error("JWT token expired: {}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT Authentication Failed: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldSkip = EXCLUDED_PATHS.stream().anyMatch(path::contains);
        log.debug("shouldNotFilter for path {}: {}", path, shouldSkip);
        return shouldSkip;
    }

    private boolean isRefreshPath(HttpServletRequest request) {
        return request.getRequestURI().contains("/api/v1/auth/refresh-token");
    }

    private String getTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> "access_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
package com.uniread.auth.filters;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.dto.response.LoginResponse;
import com.uniread.auth.service.AuthCookieService;
import com.uniread.auth.service.JsonWebTokenService;
import com.uniread.user.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonWebTokenFilter extends OncePerRequestFilter {

    private final AuthCookieService cookieService;
    private final JsonWebTokenService jsonWebTokenService;
    private final CustomUserDetailsService customUserDetailsService;

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/auth/login",
            "/auth/register"
    );

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String accessToken = getTokenFromCookies(cookies, "access_token");

            if (accessToken != null && !jsonWebTokenService.isTokenExpired(accessToken)
                    && jsonWebTokenService.isAccessToken(accessToken)) {
                useAccessToken(request, accessToken);
                filterChain.doFilter(request, response);
                return;
            }

            String refreshToken = getTokenFromCookies(cookies, "refresh_token");
            if (refreshToken != null && !jsonWebTokenService.isTokenExpired(refreshToken)
                    && jsonWebTokenService.isRefreshToken(refreshToken)) {
                boolean refreshed = triggerRefreshToken(request, response, refreshToken);
                if (refreshed) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            cookieService.clearAuthCookies(response);

        } catch (ExpiredJwtException e) {
            log.debug("JWT expired: {}", e.getMessage());
            // cookieService.clearAuthCookies(response);
        } catch (Exception e) {
            log.error("JWT authentication failed: {}", e.getMessage(), e);
            // cookieService.clearAuthCookies(response);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDED_PATHS.stream().anyMatch(path::contains);
    }


    private String getTokenFromCookies(Cookie[] cookies, String tokenName) {
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> tokenName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }


    private boolean triggerRefreshToken(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String refreshToken) {

        UUID userId = jsonWebTokenService.extractUserId(refreshToken);
        if (userId == null) {
            log.warn("Refresh token missing userId claim");
            return false;
        }

        CustomUserDetails userDetails;
        try {
            userDetails = (CustomUserDetails) customUserDetailsService.loadUserById(userId);
        } catch (Exception e) {
            log.warn("Could not load user {} from refresh token: {}", userId, e.getMessage());
            return false;
        }

        // Ban check only on refresh -> DB hit already happening
        if (userDetails.isCurrentlyBanned()) {
            log.info("Banned user {} attempted refresh", userId);
            cookieService.clearAuthCookies(response);
            return false;
        }

        assignContext(request, userDetails);

        var loginResponse = LoginResponse.builder()
                .success(true)
                .message("Refreshed session successful")
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .displayName(userDetails.getDisplayName())
                .email(userDetails.getEmail())
                .emailVerifiedAt(userDetails.getEmailVerifiedAt())
                .bannedAt(userDetails.getBannedAt())
                .unbannedAt(userDetails.getBannedAt())
                .deletedAt(userDetails.getDeletedAt())
                .authorities(userDetails.getAuthorities())
                .build();

        cookieService.setAuthCookies(response, loginResponse);
        return true;
    }

    private void useAccessToken(HttpServletRequest request, String accessToken) {
        var userDetails = buildUserDetails(accessToken);
        assignContext(request, userDetails);
    }

    private void assignContext(HttpServletRequest request, CustomUserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

    }

    private CustomUserDetails buildUserDetails(String token) {
        var payload = jsonWebTokenService.extractAllClaims(token);
        List<String> rawRoles = payload.get("roles", List.class);

        List<GrantedAuthority> authorities = rawRoles == null
                ? List.of()
                : rawRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());


        return CustomUserDetails.builder()
                .id(UUID.fromString(payload.getSubject()))
                .email(payload.get("email", String.class))
                .username(payload.get("username", String.class))
                .displayName(payload.get("displayName", String.class))

                .emailVerifiedAt(instantClaim(payload, "emailVerifiedAt"))
                .bannedAt(instantClaim(payload, "bannedAt"))
                .unbannedAt(instantClaim(payload, "unbannedAt"))
                .deletedAt(instantClaim(payload, "deletedAt"))
                .authorities(authorities)
                .build();

    }

    private Instant instantClaim(Claims claims, String key) {
        Object raw = claims.get(key);
        if (raw == null) return null;
        if (raw instanceof String s) return Instant.parse(s);
        if (raw instanceof Number n) return Instant.ofEpochSecond(n.longValue()); // legacy tokens
        throw new IllegalStateException("Unexpected " + key + " claim type: " + raw.getClass());
    }

}
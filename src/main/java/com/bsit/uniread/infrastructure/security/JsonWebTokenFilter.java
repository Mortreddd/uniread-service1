package com.bsit.uniread.infrastructure.security;

import com.bsit.uniread.application.services.auth.JsonWebTokenService;
import com.bsit.uniread.application.services.user.CustomUserDetailsService;
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
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonWebTokenFilter extends OncePerRequestFilter {

    private final JsonWebTokenService jsonWebTokenService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String accessToken = getTokenFromCookies(cookies);


        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            UUID userId = jsonWebTokenService.extractUserId(accessToken);
            if (SecurityContextHolder.getContext().getAuthentication() == null
                    && jsonWebTokenService.validateToken(accessToken, userId)) {

                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"error\":\"JWT expired\",\"message\":\"" + e.getMessage() + "\"}");
            return;
        } catch (Exception e) {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"error\":\"Authentication failed\",\"message\":\"" + e.getMessage() + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.contains("/api/v1/auth/refresh-token") ||
                path.contains("/api/v1/auth/login") ||
                path.contains("/api/v1/auth/register");
    }

    private String getTokenFromCookies(Cookie[] cookies) {
        if(cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> "access_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

}

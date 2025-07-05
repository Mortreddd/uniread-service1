package com.bsit.uniread.infrastructure.security;

import com.bsit.uniread.application.services.auth.JsonWebTokenService;
import com.bsit.uniread.application.services.user.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
        String authHeader = request.getHeader("Authorization");
        log.debug("Auth Header: {}", authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            // Get the userId from a jwt token
            UUID userId = jsonWebTokenService.extractUserId(token);
            log.info("Extracted userId: {}", userId.toString());
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                // The user details will be UUID of userId
                // Used a custom User Details to load the user immediately using the userId
                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                log.info("User Details: {}", userDetails.getUsername());
                if (jsonWebTokenService.validateToken(token, userId)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("SecurityContextHolder : {}", SecurityContextHolder.getContext());
                }
            }
        } catch (ExpiredJwtException e) {
            log.error("JWT token expired: {}", e.getMessage());
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"error\":\"JWT expired\",\"message\":\"" + e.getMessage() + "\"}");
            return;
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"error\":\"Authentication failed\",\"message\":\"" + e.getMessage() + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

}

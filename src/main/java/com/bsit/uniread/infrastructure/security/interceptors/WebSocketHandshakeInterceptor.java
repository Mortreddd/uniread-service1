package com.bsit.uniread.infrastructure.security.interceptors;

import com.bsit.uniread.application.services.auth.JsonWebTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final JsonWebTokenService jsonWebTokenService;

    public WebSocketHandshakeInterceptor(JsonWebTokenService jsonWebTokenService) {
        this.jsonWebTokenService = jsonWebTokenService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        log.debug("------------------- ServerHttpRequest {}", request.toString());

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;

            // Try to get token from query parameter first
            String token = servletRequest.getServletRequest().getParameter("access_token");
            log.debug("------------------------------------- Auth Token {}", token);
            if (token != null && !token.isBlank()) {
                try {
                    UUID userId = jsonWebTokenService.extractUserId(token);
                    attributes.put("user", (Principal) userId::toString);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Optional post-handshake logic
    }
}
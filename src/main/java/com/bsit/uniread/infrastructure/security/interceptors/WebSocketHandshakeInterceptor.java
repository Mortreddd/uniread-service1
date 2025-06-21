package com.bsit.uniread.infrastructure.security.interceptors;

import com.bsit.uniread.application.services.auth.JsonWebTokenService;
import com.bsit.uniread.application.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

@Slf4j
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final JsonWebTokenService jsonWebTokenService;
    private final UserService userService;

    public WebSocketHandshakeInterceptor(JsonWebTokenService service, UserService userService) {
        this.jsonWebTokenService = service;
        this.userService = userService;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws Exception {
        log.debug("Handshake initiated for: {}", request.getURI());
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String token = servletRequest.getServletRequest().getParameter("access_token");
            log.debug("Token found: {}", token);

            if (token != null) {
                UUID userId = jsonWebTokenService.extractUserId(token);
                log.debug("Authenticating user: {}", userId);

                attributes.put("user", (Principal) userId::toString);
                return true;
            }
        }

        return false;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {}
}

package com.bsit.uniread.infrastructure.security.interceptors;

import com.bsit.uniread.application.services.auth.JsonWebTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final JsonWebTokenService jsonWebTokenService;


    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
           Map<String, Object> attributes
    ) throws Exception {
        if(request instanceof ServletServerHttpRequest servletServerHttpRequest) {

            HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();

            String accessToken = getTokenFromCookies(httpServletRequest.getCookies());

            if (accessToken != null) {

                UUID userId = jsonWebTokenService.extractUserId(accessToken);
                attributes.put("user", userId.toString());
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {}

    private String getTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> "access_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

}

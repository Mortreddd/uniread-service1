package com.bsit.uniread.infrastructure.configurations;

import com.bsit.uniread.application.services.auth.JsonWebTokenService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.infrastructure.security.interceptors.WebSocketHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final UserService userService;
    private final JsonWebTokenService jsonWebTokenService;
    @Value("${client.url}")
    private String clientUrl;


    private static final String API_PATH = "/ws";
    /**
     * Register the prefixes of all the topics
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * Register every endpoint for the websockets
     * Example: Messages, Notifications for real time
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/messages")
                .addInterceptors(new WebSocketHandshakeInterceptor(jsonWebTokenService, userService))
                .setHandshakeHandler(defaultHandshakeHandler())
                .setAllowedOriginPatterns(clientUrl)
                .withSockJS();

        registry.addEndpoint("/ws/conversations")
                .addInterceptors(new WebSocketHandshakeInterceptor(jsonWebTokenService, userService))
                .setHandshakeHandler(defaultHandshakeHandler())
                .setAllowedOriginPatterns(clientUrl)
                .withSockJS();

        registry.addEndpoint("/ws/notifications")
                .addInterceptors(new WebSocketHandshakeInterceptor(jsonWebTokenService, userService))
                .setHandshakeHandler(defaultHandshakeHandler())
                .setAllowedOriginPatterns(clientUrl)
                .withSockJS();
    }

    private DefaultHandshakeHandler defaultHandshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                return (Principal) attributes.get("user");
            }
        };
    }
}

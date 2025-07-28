package com.bsit.uniread.infrastructure.configurations;

import com.bsit.uniread.application.services.auth.JsonWebTokenService;
import com.bsit.uniread.infrastructure.security.handlers.CustomHandshakeHandler;
import com.bsit.uniread.infrastructure.security.interceptors.WebSocketHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final JsonWebTokenService jsonWebTokenService;
    @Value("${client.url}")
    private String clientUrl;


    /**
     * Register the prefixes of all the topics
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue", "/user");
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
        String[] endpoints = new String[]{"messages", "conversations", "notifications"};
        for (String endpoint: endpoints) {
            registry.addEndpoint(String.format("ws/%s", endpoint))
                    .addInterceptors(new WebSocketHandshakeInterceptor(jsonWebTokenService))
                    .setHandshakeHandler(defaultHandshakeHandler())
                    .setAllowedOriginPatterns(clientUrl)
                    .withSockJS();
        }
    }

    private DefaultHandshakeHandler defaultHandshakeHandler() {
        return new CustomHandshakeHandler();
    }
}

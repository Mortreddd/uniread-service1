package com.bsit.uniread.infrastructure.configurations;

import com.bsit.uniread.infrastructure.security.interceptors.JsonWebTokenChannelInterceptor;
import com.bsit.uniread.infrastructure.security.interceptors.JsonWebTokenHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Value("${client.url}")
    private String clientUrl;
    private final JsonWebTokenChannelInterceptor jsonWebTokenChannelInterceptor;
    private final JsonWebTokenHandshakeInterceptor jsonWebTokenHandshakeInterceptor;
//    private final CustomHandshakeHandler customHandshakeHandler;


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
        registry.addEndpoint("/ws")
                .addInterceptors(jsonWebTokenHandshakeInterceptor)
                .setAllowedOriginPatterns(clientUrl);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jsonWebTokenChannelInterceptor);
    }

}

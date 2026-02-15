package com.bsit.uniread.infrastructure.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@EnableWebSocketSecurity
public class WebSocketSecurityConfiguration {

    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        return messages
                .simpDestMatchers("/app/**").authenticated()
                .simpSubscribeDestMatchers("/user/**", "/topic/**", "/queue/**").authenticated()
                .anyMessage().denyAll()
                .build();
    }

    @Bean("csrfChannelInterceptor")
    public ChannelInterceptor channelInterceptor() {
        return new ChannelInterceptor() {};
    }
}
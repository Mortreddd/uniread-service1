package com.bsit.uniread.infrastructure.security.interceptors;

import com.bsit.uniread.application.services.auth.JsonWebTokenService;
import com.bsit.uniread.application.services.user.CustomUserDetailsService;
import com.bsit.uniread.infrastructure.security.validations.websocket.WebsocketValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final Map<String, WebsocketValidator> validators;

    @Autowired
    public WebSocketChannelInterceptor(
            List<WebsocketValidator> validators
    ) {
        this.validators = validators.stream()
                .collect(Collectors.toMap(WebsocketValidator::getPrefix, v -> v));
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

            if (sessionAttributes == null) {
                return null;
            }

            String userId = (String) sessionAttributes.get("user");
            if (userId != null) {
                accessor.setUser(() -> userId);
                this.validators.values().forEach(v -> {
                    v.validate(accessor);
                });
            }

        }
        return message;
    }

}
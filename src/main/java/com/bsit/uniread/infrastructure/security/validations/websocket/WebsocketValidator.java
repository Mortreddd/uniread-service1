package com.bsit.uniread.infrastructure.security.validations.websocket;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public interface WebsocketValidator {
    String getPrefix();

    void validate(StompHeaderAccessor accessor);
}

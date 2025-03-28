package com.bsit.uniread.infrastructure.handler.publishers.auth;

import com.bsit.uniread.domain.events.auth.GoogleAuthenticationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticationPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishOAuthEvent(ApplicationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}

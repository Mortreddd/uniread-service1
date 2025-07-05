package com.bsit.uniread.infrastructure.handler.publishers.auth;

import com.bsit.uniread.application.dto.response.auth.GoogleUserInfoResponse;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.auth.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegistrationPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishUserRegistration(User registeredUser) {
        applicationEventPublisher.publishEvent(new UserRegistrationEvent(this, registeredUser));
    }
}

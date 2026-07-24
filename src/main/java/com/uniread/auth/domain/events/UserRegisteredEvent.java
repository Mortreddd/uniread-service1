package com.uniread.auth.domain.events;

import com.uniread.auth.dto.request.UserRegistrationRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {

    private final UUID userId;
    private final UserRegistrationRequest request;

    public UserRegisteredEvent(UUID userId, UserRegistrationRequest request) {
        super(userId);
        this.userId = userId;
        this.request = request;
    }
}

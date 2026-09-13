package com.uniread.auth.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class VerifyAccountEvent extends ApplicationEvent {

    private final UUID userId;
    private final String email;
    private final String username;

    public VerifyAccountEvent(UUID userId, String email, String username) {
        super(userId);
        this.userId = userId;
        this.email = email;
        this.username = username;
    }

}

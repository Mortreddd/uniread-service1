package com.uniread.auth.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class ForgotPasswordEvent extends ApplicationEvent {

    private final UUID userId;
    private final String email;
    private final String displayName;

    public ForgotPasswordEvent(UUID userId, String email, String displayName) {
        super(userId);
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
    }
}

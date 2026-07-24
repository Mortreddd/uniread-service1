package com.uniread.user.domain.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class UpdateEmailEvent extends ApplicationEvent {

    private final UUID userId;
    private final String email;
    private final String username;

    public UpdateEmailEvent(UUID userId, String email, String username) {
        super(userId);
        this.userId = userId;
        this.email = email;
        this.username = username;
    }




}

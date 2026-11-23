package com.uniread.notification.domain.events;

import com.uniread.notification.domain.entities.Notification;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class NewInAppCreatedEvent extends ApplicationEvent {


    private final UUID userId;
    private final Notification notification;


    public NewInAppCreatedEvent(UUID userId, Notification notification) {
        super(userId);
        this.userId = userId;
        this.notification = notification;

    }

}

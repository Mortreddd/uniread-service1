package com.uniread.auth.domain.events;

import com.uniread.user.domain.entities.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConfirmedEmailEvent extends ApplicationEvent {

    private final User user;

    public ConfirmedEmailEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}

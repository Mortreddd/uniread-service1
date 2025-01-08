package com.bsit.uniread.domain.events.auth;


import com.bsit.uniread.domain.entities.user.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegistrationEvent extends ApplicationEvent {

    private final User user;

    public UserRegistrationEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

}

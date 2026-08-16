package com.uniread.auth.domain.events;

import com.uniread.auth.dto.response.GoogleUserInfoResponse;
import com.uniread.auth.domain.entities.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GoogleRegistrationEvent extends ApplicationEvent {

    private final User user;
    private final GoogleUserInfoResponse response;

    public GoogleRegistrationEvent(Object source, User user, GoogleUserInfoResponse response) {
        super(source);
        this.user = user;
        this.response = response;
    }
}

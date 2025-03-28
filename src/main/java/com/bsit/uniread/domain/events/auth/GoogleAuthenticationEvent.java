package com.bsit.uniread.domain.events.auth;

import com.bsit.uniread.application.dto.response.auth.GoogleUserInfoResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GoogleAuthenticationEvent extends ApplicationEvent {

    private final GoogleUserInfoResponse userInfo;

    public GoogleAuthenticationEvent(Object source, GoogleUserInfoResponse userInfo) {
        super(source);
        this.userInfo = userInfo;
    }
}

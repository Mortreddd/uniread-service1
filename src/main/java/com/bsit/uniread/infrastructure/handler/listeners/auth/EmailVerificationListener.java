package com.bsit.uniread.infrastructure.handler.listeners.auth;

import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.auth.EmailConfirmationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationListener implements ApplicationListener<EmailConfirmationEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(EmailConfirmationEvent event) {
        User user = event.getUser();
        userService.verifyUserEmail(user);
    }
}

package com.bsit.uniread.infrastructure.handler.listeners.auth;

import com.bsit.uniread.application.constants.EmailMessages;
import com.bsit.uniread.application.services.otp.OtpService;
import com.bsit.uniread.domain.events.auth.UserRegistrationEvent;
import com.bsit.uniread.application.services.auth.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRegistrationListener implements ApplicationListener<UserRegistrationEvent> {

    private final EmailService emailService;
    private final OtpService otpService;

    @Override
    @SneakyThrows
    @Transactional
    public void onApplicationEvent(final UserRegistrationEvent event) {
        final String email = event.getUser().getEmail();
        final UUID otpUuid = otpService.generateOtp(email).getId();
        emailService.sendVerificationUrlEmail(email, otpUuid);
    }
}

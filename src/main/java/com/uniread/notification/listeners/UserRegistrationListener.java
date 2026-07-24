package com.uniread.notification.listeners;

import com.uniread.auth.domain.events.NewVerifiedUserEvent;
import com.uniread.auth.domain.events.UserRegisteredEvent;
import com.uniread.notification.domain.entities.NotificationChannel;
import com.uniread.notification.domain.entities.NotificationMessage;
import com.uniread.notification.domain.entities.NotificationType;
import com.uniread.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserRegistrationListener {

    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRegister(UserRegisteredEvent event) {
        emailService.sendEmailConfirmation(
                event.getUserId(),
                event.getRequest().getEmail(),
                event.getRequest().getUsername(),
                NotificationType.EMAIL_CONFIRMATION
        );
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onVerify(NewVerifiedUserEvent event) {

        Map<String, Object> variables = Map.of(
                "userId", event.getUserId(),
                "email", event.getEmail(),
                "username", event.getUsername()
        );
        var message = NotificationMessage.builder()
                .type(NotificationType.WELCOME)
                .userId(event.getUserId())
                .variables(variables)
                .channels(Set.of(NotificationChannel.EMAIL))
                .build();

        emailService.broadcast(message);

    }

}

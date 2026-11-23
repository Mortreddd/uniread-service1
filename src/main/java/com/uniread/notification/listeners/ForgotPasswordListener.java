package com.uniread.notification.listeners;

import com.uniread.auth.domain.events.ForgotPasswordEvent;
import com.uniread.auth.service.VerificationTokenService;
import com.uniread.notification.domain.entities.NotificationChannel;
import com.uniread.notification.domain.entities.NotificationMessage;
import com.uniread.notification.domain.entities.NotificationType;
import com.uniread.notification.service.Broadcaster;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ForgotPasswordListener {

    @Value("${client.url}")
    private String clientUrl;
    private final VerificationTokenService tokenService;
    private final Broadcaster broadcaster;


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onForgotPassword(ForgotPasswordEvent event) {
        var vt = tokenService.generateVerificationToken(event.getUserId(), event.getEmail());

        var verificationLink = clientUrl + "/auth/change-password?token=" + vt.getToken();
        Map<String, Object> variables = Map.of(
                "email", event.getEmail(),
                "verificationLink", verificationLink
        );

        var message = NotificationMessage.builder()
                .userId(event.getUserId())
                .type(NotificationType.FORGOT_PASSWORD)
                .targetId(null)
                .variables(variables)
                .channels(Set.of(NotificationChannel.EMAIL))
                .build();

        broadcaster.broadcast(message);
    }

}

package com.uniread.notification.service;

import com.uniread.auth.service.VerificationTokenService;
import com.uniread.notification.domain.entities.NotificationChannel;
import com.uniread.notification.domain.entities.NotificationMessage;
import com.uniread.notification.domain.entities.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthEmailService {

    @Value("${client.url}")
    private String clientUrl;

    private final Broadcaster broadcaster;
    private final VerificationTokenService verificationTokenService;

    public void sendEmailConfirmation(UUID userId, String email, String username, NotificationType type) {
        var vt = verificationTokenService.generateVerificationToken(userId, email);
        var verificationLink = clientUrl + "/auth/verify-email?token=" + vt.getToken();

        Map<String, Object> variables = Map.of(
                "userId", userId,
                "email", email,
                "username", username,
                "verificationLink", verificationLink
        );

        var message = NotificationMessage.builder()
                .targetId(null)
                .userId(userId)
                .channels(Set.of(NotificationChannel.EMAIL))
                .type(type)
                .variables(variables)
                .build();

        broadcaster.broadcast(message);
    }

}
package com.uniread.notification.service;

import com.uniread.auth.domain.entities.VerificationToken;
import com.uniread.auth.repositories.VerificationTokenRepository;
import com.uniread.common.utils.DateUtil;
import com.uniread.notification.domain.entities.NotificationChannel;
import com.uniread.notification.domain.entities.NotificationMessage;
import com.uniread.notification.domain.entities.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${client.url}")
    private String clientUrl;
    
    private final VerificationTokenRepository tokenRepository;
    private final List<NotificationSender> notificationSenders;

    public void sendEmailConfirmation(UUID userId, String email, String username, NotificationType type) {
        var vt = generateVerificationToken(userId, email);
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

        broadcast(message);
    }



    private Instant getExpiryTime() {
        return DateUtil.now().plus(15, ChronoUnit.MINUTES);
    }

    private VerificationToken generateVerificationToken(UUID userId, String email) {
        return tokenRepository.save(
                VerificationToken.builder()
                        .token(UUID.randomUUID().toString())
                        .used(false)
                        .email(email)
                        .expiryDate(getExpiryTime())
                        .userId(userId)
                        .build()
        );
    }

    public void broadcast(NotificationMessage message) {
        notificationSenders.forEach(sender -> {
            if (message.getChannels().contains(sender.getChannel())) {
                sender.send(message);
            }
        });
    }
}
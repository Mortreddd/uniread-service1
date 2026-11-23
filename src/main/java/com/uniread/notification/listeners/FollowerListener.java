package com.uniread.notification.listeners;

import com.uniread.auth.domain.entities.User;
import com.uniread.notification.domain.entities.Notification;
import com.uniread.notification.domain.entities.NotificationChannel;
import com.uniread.notification.domain.entities.NotificationMessage;
import com.uniread.notification.domain.entities.NotificationType;
import com.uniread.notification.repositories.NotificationRepository;
import com.uniread.notification.service.NotificationService;
import com.uniread.notification.service.SocialSenderService;
import com.uniread.social.domain.events.NewFollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FollowerListener {

    private final NotificationService notificationService;
    private final SocialSenderService senderService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReceiveFollow(NewFollowerEvent event) {
        var followingId = event.getFollowingId();

        Map<String, Object> variables = Map.of(
                "username", event.getUsername(),
                "displayName", event.getDisplayName()
        );

        var notificationMessage = event.getDisplayName() + " started follow you";
        var message = NotificationMessage.builder()
                .targetId(null)
                .userId(followingId)
                .message(notificationMessage)
                .channels(Set.of(NotificationChannel.IN_APP))
                .type(NotificationType.NEW_FOLLOWER)
                .variables(variables)
                .build();

        senderService.createNotification(message);
        senderService.forward(message);

    }

}

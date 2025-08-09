package com.bsit.uniread.application.services.notification;

import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BroadcastNotifier implements Notifier {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void handleNotify(List<User> users, NotificationDto notification) {
        sendBroadcastNotification(users, notification);
    }

    private void sendBroadcastNotification(List<User> users, NotificationDto notification) {
        users.forEach(user -> sendBroadcast(user, notification));
    }

    private void sendBroadcast(User user, NotificationDto notification) {
        simpMessagingTemplate.convertAndSendToUser(
                user.getId().toString(),
                "/topic/notifications",
                notification
        );
    }
}
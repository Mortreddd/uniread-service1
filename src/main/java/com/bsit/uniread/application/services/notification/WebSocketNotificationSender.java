package com.bsit.uniread.application.services.notification;

import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationSender {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendUsersNotifications(List<User> users, List<NotificationDto> notifications) {
        notifications.forEach(notification -> {
            sendUsersNotification(users, notification);
        });
    }

    private void sendUsersNotification(List<User> users, NotificationDto notification) {
        users.forEach(user -> {
            sendNotification(user, notification);
        });
    }

    private void sendNotification(User user, NotificationDto notification) {
        simpMessagingTemplate.convertAndSendToUser(
                user.getId().toString(),
                "/topic/notifications",
                notification
        );
    }

}

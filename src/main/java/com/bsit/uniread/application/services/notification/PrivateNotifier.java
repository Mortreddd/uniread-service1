package com.bsit.uniread.application.services.notification;

import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

public class PrivateNotifier extends Notifier {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public PrivateNotifier(List<User> users, List<NotificationDto> notifications, SimpMessagingTemplate simpMessagingTemplate) {
        super(users, notifications);
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    private void sendPrivateUsersNotification(List<User> users, NotificationDto notification) {
        users.forEach(user -> sendNotification(user, notification));
    }

    private void sendNotification(User user, NotificationDto notification) {
        simpMessagingTemplate.convertAndSendToUser(
                user.getId().toString(),
                "/queue/notifications",
                notification
        );
    }

    @Override
    public void handleNotify() {
        getNotifications().forEach(notification -> sendPrivateUsersNotification(getUsers(), notification));
    }
}

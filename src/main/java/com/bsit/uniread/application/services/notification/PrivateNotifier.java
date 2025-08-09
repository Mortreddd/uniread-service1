package com.bsit.uniread.application.services.notification;

import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateNotifier implements Notifier {

    private SimpMessagingTemplate simpMessagingTemplate;

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
    public void handleNotify(List<User> users, NotificationDto notification) {
        sendPrivateUsersNotification(users, notification);
    }

}

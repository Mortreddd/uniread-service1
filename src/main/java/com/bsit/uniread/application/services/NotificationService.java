package com.bsit.uniread.application.services;

import com.bsit.uniread.domain.entities.Notification;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification createNotification(User receiver, String title, String description) {
        return notificationRepository.save(
                Notification.builder()
                        .title(title)
                        .description(description)
                        .user(receiver)
                        .build()
        );
    }

    public void notifyUsers(String title, String description, List<User> users) {
        List<Notification> notifications = users.stream()
                .map(user ->
                        Notification.builder()
                            .title(title)
                            .description(description)
                            .user(user)
                            .build()
                        )
                .toList();

        notificationRepository.saveAll(notifications);
    }
}

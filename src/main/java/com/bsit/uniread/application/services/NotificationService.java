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

    public void notifyUser(String title, String description, User user) {
        Notification notification = Notification.builder()
                .title(title)
                .description(description)
                .user(user)
                .sender(null)
                .build();

        notificationRepository.save(notification);
    }

    public void notifyUser(String title, String description, User user, User sender) {
        Notification notification = Notification.builder()
                .title(title)
                .description(description)
                .user(user)
                .sender(null)
                .build();

        notificationRepository.save(notification);
    }

    public void notifyUsers(String title, String description, List<User> users) {
        List<Notification> notifications = users.stream()
                .map(user ->
                        Notification.builder()
                            .title(title)
                            .description(description)
                            .user(user)
                            .sender(null)
                            .build()
                        )
                .toList();

        notificationRepository.saveAll(notifications);
    }

    public void notifyUsers(String title, String description, List<User> users, User sender) {
        List<Notification> notifications = users.stream()
                .map(user ->
                        Notification.builder()
                                .title(title)
                                .description(description)
                                .user(user)
                                .sender(sender)
                                .build()
                )
                .toList();

        notificationRepository.saveAll(notifications);
    }
}

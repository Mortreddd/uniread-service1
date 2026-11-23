package com.uniread.notification.service;

import com.uniread.auth.domain.entities.User;
import com.uniread.notification.domain.entities.Notification;
import com.uniread.notification.domain.entities.NotificationMessage;
import com.uniread.notification.dto.response.NotificationDto;
import com.uniread.notification.mappers.NotificationMapper;
import com.uniread.notification.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialSenderService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper mapper;
    private final Broadcaster broadcaster;

    @Transactional
    public void createNotification(NotificationMessage message) {
        var notification = Notification.builder()
                .user(User.builder().id(message.getUserId()).build())
                .title(message.getType().getDefaultTitle())
                .description(message.getMessage())
                .metadata(message.getVariables())
                .type(message.getType())
                .targetType(message.getTargetType())
                .build();

        notificationRepository.save(notification);
    }

    public void forward(NotificationMessage message) {
        broadcaster.broadcast(message);
    }
}

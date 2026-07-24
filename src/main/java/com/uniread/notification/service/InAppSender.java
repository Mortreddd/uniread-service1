package com.uniread.notification.service;

import com.uniread.notification.domain.entities.Notification;
import com.uniread.notification.domain.entities.NotificationChannel;
import com.uniread.notification.domain.entities.NotificationMessage;
import com.uniread.notification.repositories.NotificationRepository;
import com.uniread.user.domain.entities.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InAppSender implements NotificationSender {

    private final NotificationRepository notificationRepository;

    @Override
    public void send(NotificationMessage message) {
        notificationRepository.save(
                Notification.builder()
                        .user(User.builder().id(message.getUserId()).build())
                        .title(message.getType().getDefaultTitle())
                        .description(message.getType().getDefaultDescription())
                        .metadata(message.getVariables())
                        .type(message.getType())
                        .targetType(message.getTargetType())
                        .build()
        );


        log.info("InAppSender received notification message: {}", message);

    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.IN_APP;
    }
}

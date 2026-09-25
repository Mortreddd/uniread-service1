package com.uniread.notification.service;

import com.uniread.common.exceptions.ValidationException;
import com.uniread.notification.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationValidator {

    private final NotificationRepository repository;

    public void isOwner(UUID notificationId, UUID userId) {
        if(notificationId == null) {
            throw new ValidationException("Notification is required");
        }

        if(userId == null) {
            throw new ValidationException("User is required");
        }

        if(!repository.isOwner(notificationId, userId)) {
            throw new ValidationException("User is not the owner of the notification");
        }
    }
}

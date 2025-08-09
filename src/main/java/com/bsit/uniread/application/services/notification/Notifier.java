package com.bsit.uniread.application.services.notification;

import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.domain.entities.user.User;

import java.util.List;

public interface Notifier {
    public void handleNotify(List<User> users, NotificationDto notification);
}

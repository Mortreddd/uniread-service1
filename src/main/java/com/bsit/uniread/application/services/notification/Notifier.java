package com.bsit.uniread.application.services.notification;

import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.domain.entities.user.User;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class Notifier {

    private List<User> users;
    private List<NotificationDto> notifications;

    public Notifier(List<User> users, List<NotificationDto> notifications) {
        this.users = users;
        this.notifications = notifications;
    }

    public abstract void handleNotify();
}

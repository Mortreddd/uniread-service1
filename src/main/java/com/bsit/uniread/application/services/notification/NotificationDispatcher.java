package com.bsit.uniread.application.services.notification;

import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.domain.entities.notification.Notification;
import com.bsit.uniread.domain.entities.user.User;

import java.util.List;

public class NotificationDispatcher {

    private final Notifier notifier;
    private final List<User> users;
    private final NotificationDto notification;

    public NotificationDispatcher(Notifier notifier, List<User> users, Notification notification) {
        this.notifier = notifier;
        this.users = users;
        this.notification = new NotificationDto(notification);
    }

    public void dispatch() {
        notifier.handleNotify(users, notification);
    }

}
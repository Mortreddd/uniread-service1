package com.bsit.uniread.application.services.notification;

import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.domain.entities.notification.Notification;
import com.bsit.uniread.domain.entities.user.User;

import java.util.ArrayList;
import java.util.List;

public class NotificationDispatcher {

    private final List<Notifier> notifiers;

    public NotificationDispatcher() {
        this.notifiers = new ArrayList<>();
    }

    public void addNotifier(Notifier notifier) {
        notifiers.add(notifier);
    }

    public void dispatch() {
        notifiers.forEach(Notifier::handleNotify);
    }

}
package com.uniread.notification.service;

import com.uniread.notification.domain.entities.NotificationChannel;
import com.uniread.notification.domain.entities.NotificationMessage;

public interface NotificationSender {

    void send(NotificationMessage message);
    NotificationChannel getChannel();

}

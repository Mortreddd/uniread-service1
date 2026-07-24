package com.uniread.notification.service;

import com.uniread.notification.domain.entities.Notification;
import com.uniread.notification.domain.entities.NotificationChannel;
import com.uniread.notification.domain.entities.NotificationMessage;
import com.uniread.notification.repositories.NotificationRepository;
import com.uniread.user.domain.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final List<NotificationSender> senders;

    @Transactional
    public void notify(NotificationMessage message) {

        for(var sender : senders) {
            if(message.getChannels().contains(sender.getChannel())) {
                sender.send(message);
            }
        }

    }

}

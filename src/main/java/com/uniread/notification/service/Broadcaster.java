package com.uniread.notification.service;

import com.uniread.notification.domain.entities.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Broadcaster {

    private final List<NotificationSender> senders;

    public void broadcast(NotificationMessage message) {
        senders.forEach(sender -> {
            if(message.getChannels().contains(sender.getChannel())) {
                sender.send(message);
            }
        });
    }
}

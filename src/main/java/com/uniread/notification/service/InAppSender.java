package com.uniread.notification.service;

import com.uniread.notification.domain.entities.NotificationChannel;
import com.uniread.notification.domain.entities.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InAppSender implements NotificationSender {

    private final ServerSentStreamService streamService;

    @Override
    public void send(NotificationMessage message) {

        streamService.subscribe(message.getUserId());
        streamService.send(message.getUserId(), message);

        log.info("InAppSender received notification message: {}", message);

    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.IN_APP;
    }
}

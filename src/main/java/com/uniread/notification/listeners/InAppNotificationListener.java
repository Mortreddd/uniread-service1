package com.uniread.notification.listeners;

import com.uniread.notification.domain.events.NewInAppCreatedEvent;
import com.uniread.notification.service.ServerSentStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class InAppNotificationListener {

    private final ServerSentStreamService service;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onListen(NewInAppCreatedEvent event) {

        service.send(
                event.getUserId(),
                event.getNotification()
        );

    }
}

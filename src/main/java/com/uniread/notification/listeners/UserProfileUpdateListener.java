package com.uniread.notification.listeners;

import com.uniread.notification.domain.entities.NotificationType;
import com.uniread.notification.service.AuthEmailService;
import com.uniread.user.domain.events.UpdateEmailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserProfileUpdateListener {

    private final AuthEmailService authEmailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUpdateEmail(UpdateEmailEvent event) {
        authEmailService.sendEmailConfirmation(
                event.getUserId(),
                event.getEmail(),
                event.getUsername(),
                NotificationType.EMAIL_UPDATE_CONFIRMATION
        );
    }
}

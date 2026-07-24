package com.uniread.notification.listeners;

import com.uniread.notification.service.EmailService;
import com.uniread.user.domain.events.UpdateEmailEvent;
import com.uniread.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserProfileUpdateListener {

    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUpdateEmail(UpdateEmailEvent event) {
        emailService.sendEmailConfirmation(
                event.getUserId()
        );
    }
}

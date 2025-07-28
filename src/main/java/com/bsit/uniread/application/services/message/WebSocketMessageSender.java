package com.bsit.uniread.application.services.message;

import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.message.UserSentMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketMessageSender {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserSentMessageEvent event) {
        try {
            log.info("Processing UserSentMessageEvent for message {}", event.getMessage().getId());
            List<User> participants = event.getParticipants()
                    .stream()
                    .map(Participant::getUser)
                    .toList();

            sendUsersMessage(participants, event.getMessage());
            log.info("Successfully processed message {}", event.getMessage().getId());
        } catch (Exception e) {
            log.error("Failed to process UserSentMessageEvent", e);
        }
    }

    private void sendUsersMessage(List<User> users, MessageDto message) {
        users.forEach(user -> sendUserMessage(user, message));
    }

    private void sendUserMessage(User user, MessageDto message) {
        if(user == null) return;

        simpMessagingTemplate.convertAndSendToUser(
                user.getId().toString(),
                "/queue/messages",
                message
        );
    }

}

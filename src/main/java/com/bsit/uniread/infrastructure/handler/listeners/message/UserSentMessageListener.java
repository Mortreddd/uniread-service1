package com.bsit.uniread.infrastructure.handler.listeners.message;

import com.bsit.uniread.application.services.message.ParticipantService;
import com.bsit.uniread.domain.events.message.UserSentMessageEvent;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserSentMessageListener implements ApplicationListener<UserSentMessageEvent> {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ParticipantService participantService;

    @Override
    public void onApplicationEvent(UserSentMessageEvent event) {

        event.getParticipants().forEach((participant) -> {

            participant.setReadAt(DateUtil.now());
            participantService.save(participant);

            simpMessagingTemplate.convertAndSendToUser(
                    participant.getUser()
                            .getId()
                            .toString(),
                    "/queue/messages",
                    event.getMessage()
            );
        });
    }
}

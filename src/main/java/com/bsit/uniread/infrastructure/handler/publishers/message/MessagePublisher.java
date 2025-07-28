package com.bsit.uniread.infrastructure.handler.publishers.message;

import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.events.message.UserSentMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishUserSentMessage(MessageDto message, List<Participant> participants) {
        applicationEventPublisher.publishEvent(new UserSentMessageEvent(this, message, participants));
    }
}

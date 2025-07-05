package com.bsit.uniread.infrastructure.handler.publishers.message;

import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.events.message.UserSentMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishUserSentMessage(Conversation conversation, MessageDto message) {
        applicationEventPublisher.publishEvent(new UserSentMessageEvent(this, conversation, message));
    }
}

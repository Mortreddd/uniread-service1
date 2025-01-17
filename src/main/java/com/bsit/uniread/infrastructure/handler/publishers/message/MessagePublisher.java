package com.bsit.uniread.infrastructure.handler.publishers.message;

import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.domain.events.message.NewMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void sendMessage(Conversation conversation, Message message) {
        applicationEventPublisher.publishEvent(new NewMessageEvent(this, conversation, message));
    }
}

package com.bsit.uniread.domain.events.message;

import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Message;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewMessageEvent extends ApplicationEvent {

    private final Message newMessage;
    private final Conversation conversation;

    public NewMessageEvent(Object source, Conversation conversation, Message message) {
        super(source);
        this.newMessage = message;
        this.conversation = conversation;
    }
}

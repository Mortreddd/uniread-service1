package com.bsit.uniread.domain.events.message;

import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Participant;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class UserSentMessageEvent extends ApplicationEvent {

    private final List<Participant> participants;
    private final MessageDto message;

    public UserSentMessageEvent(Object source, Conversation conversation, MessageDto newMessage) {
        super(source);
        this.participants = conversation.getParticipants();
        this.message = newMessage;
    }

}

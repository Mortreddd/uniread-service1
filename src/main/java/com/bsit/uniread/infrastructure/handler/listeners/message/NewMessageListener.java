package com.bsit.uniread.infrastructure.handler.listeners.message;

import com.bsit.uniread.application.services.message.MessageService;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.domain.events.message.NewMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;


@RequiredArgsConstructor
public class NewMessageListener implements ApplicationListener<NewMessageEvent> {

    private final MessageService messageService;

    @Override
    public void onApplicationEvent(NewMessageEvent event) {
        Conversation conversation = event.getConversation();
        Message message = event.getNewMessage();


    }
}

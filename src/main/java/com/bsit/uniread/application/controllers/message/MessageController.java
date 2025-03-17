package com.bsit.uniread.application.controllers.message;

import com.bsit.uniread.application.dto.request.message.MessageCreationRequest;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.application.services.message.MessageService;
import com.bsit.uniread.application.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class MessageController {

    private final UserService userService;
    private final MessageService messageService;

    @MessageMapping("/messages/send")
    @SendTo("/topic/messages")
    public Message sendMessage(
            @Payload MessageCreationRequest messageCreationRequest
    ) {
        return messageService.createNewMessage(messageCreationRequest);
    }
}

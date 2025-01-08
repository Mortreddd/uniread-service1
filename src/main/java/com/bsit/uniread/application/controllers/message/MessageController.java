package com.bsit.uniread.application.controllers.message;

import com.bsit.uniread.application.dto.request.message.MessageCreationRequest;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.application.services.message.MessageService;
import com.bsit.uniread.application.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class MessageController {

    private final UserService userService;
    private final MessageService messageService;

    @MessageMapping("/messages/send")
    @SendTo("/topic/messages/receiver")
    public Message sendMessage(
            @RequestBody MessageCreationRequest messageCreationRequest
    ) {
        return messageService.createMessage(messageCreationRequest);
    }

}

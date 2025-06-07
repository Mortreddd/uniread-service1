package com.bsit.uniread.application.controllers.message;

import com.bsit.uniread.application.dto.request.message.MessageCreationRequest;
import com.bsit.uniread.application.services.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@RestController
@RequestMapping
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/messages/send")
    public void sendMessage(
            @Payload MessageCreationRequest messageCreationRequest
    ) {
        messageService.createNewMessage(messageCreationRequest);
    }
}

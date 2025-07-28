package com.bsit.uniread.application.controllers.message;

import com.bsit.uniread.application.dto.request.message.MessageCreationRequest;
import com.bsit.uniread.application.services.message.MessageService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @MessageMapping("/messages/send")
    public void sendMessage(
            @Payload MessageCreationRequest messageCreationRequest,
            Principal principal
    ) {
        log.info("Received message from {}: {}", principal.getName(), messageCreationRequest);
        User sender = userService.getUserById(UUID.fromString(principal.getName()));
        messageService.createNewMessage(messageCreationRequest, sender);
    }
}

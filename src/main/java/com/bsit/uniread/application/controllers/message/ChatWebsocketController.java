package com.bsit.uniread.application.controllers.message;

import com.bsit.uniread.application.dto.request.message.NewMessageRequest;
import com.bsit.uniread.application.dto.request.message.TypingParticipantRequest;
import com.bsit.uniread.application.services.message.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebsocketController {

    private final ChatService chatService;

    @SubscribeMapping("/chat.{conversationId}.read")
    public void newConversationReaderEvent(
            @DestinationVariable(value = "conversationId") UUID conversationId,
            Principal principal
    ) {
        if(principal == null) return;
        UUID authUserId = UUID.fromString(principal.getName());

        chatService.markParticipantAsRead(conversationId, authUserId);
    }

    @SubscribeMapping("/chat.{conversationId}.typing")
    public void newConversationParticipantTyping(
            @DestinationVariable(value = "conversationId") UUID conversationId,
            @Payload TypingParticipantRequest request,
            Principal principal
    ) {
        if(principal == null || request == null) return;
        var authUserId = UUID.fromString(principal.getName());
        chatService.markParticipantAsTyping(conversationId, authUserId, request.getTyping());
    }

    @SubscribeMapping("/chat.{conversationId}.messages")
    public void newOneToOneConversationMessage(
            @DestinationVariable(value = "conversationId") UUID conversationId,
            @Payload NewMessageRequest request,
            Principal principal
    ) {
        if(principal == null || request == null) return;
        var authUserId = UUID.fromString(principal.getName());

        chatService.insertNewOneToOneConversationMessage(request, conversationId, authUserId);
    }


}

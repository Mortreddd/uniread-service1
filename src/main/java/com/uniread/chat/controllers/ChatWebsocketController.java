package com.uniread.chat.controllers;

import com.uniread.chat.dto.request.NewMessageRequest;
import com.uniread.chat.dto.request.TypingParticipantRequest;
import com.uniread.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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


    @SubscribeMapping("/chats.{conversationId}")
    public void newConversationReaderEvent(
            @DestinationVariable(value = "conversationId") UUID conversationId,
            Principal principal
    ) {
        if(principal == null) return;
        UUID authUserId = UUID.fromString(principal.getName());

        chatService.markParticipantAsRead(conversationId, authUserId);
    }

    @MessageMapping("/chats/{conversationId}/typing")
    public void newConversationParticipantTyping(
            @DestinationVariable(value = "conversationId") UUID conversationId,
            @Payload TypingParticipantRequest request,
            Principal principal
    ) {
        if(principal == null || request == null) return;
        var authUserId = UUID.fromString(principal.getName());
        chatService.markParticipantAsTyping(conversationId, authUserId, request.getTyping());
    }

    @MessageMapping("/chats/{conversationId}/send")
    public void sendConversationMessage(
            @DestinationVariable(value = "conversationId") UUID conversationId,
            @Payload NewMessageRequest request,
            Principal principal
    ) {
        if(principal == null || request == null || conversationId == null) return;

        UUID authUserId = UUID.fromString(principal.getName());
        chatService.insertNewMessage(request, conversationId, authUserId);

    }

    /*@SubscribeMapping("/chat.{conversationId}")
    public void newConversa(
            @DestinationVariable(value = "conversationId") UUID conversationId,
            @Payload NewMessageRequest request,
            Principal principal
    ) {
        if(principal == null || request == null) return;
        var authUserId = UUID.fromString(principal.getName());

        chatService.insertNewOneToOneConversationMessage(request, conversationId, authUserId);
    }*/
}

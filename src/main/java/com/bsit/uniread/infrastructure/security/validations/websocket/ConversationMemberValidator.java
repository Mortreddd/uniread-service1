package com.bsit.uniread.infrastructure.security.validations.websocket;

import com.bsit.uniread.application.services.participant.ParticipantService;
import com.bsit.uniread.infrastructure.handler.exceptions.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConversationMemberValidator implements WebsocketValidator {

    private final ObjectProvider<ParticipantService> participantServiceObjectProvider;

    @Override
    public String getPrefix() {
        return "/topic/chat";
    }

    @Override
    public void validate(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        Principal principal = accessor.getUser();
        if(destination == null || destination.isBlank() || principal == null) return;

        UUID currentUserId = UUID.fromString(principal.getName());
        UUID conversationId = UUID.fromString(extractConversationId(destination));


        boolean isMember = participantServiceObjectProvider.getObject()
                .isParticipant(conversationId, currentUserId);

        if(isMember) return;

        throw new AccessDeniedException(
                "User " + currentUserId + " not allowed in conversation " + conversationId
        );
    }

    private String extractConversationId(String destination) {

        String part = destination.replace("/topic/chat.", "");

        // part = "42.messages"
        return part.split("\\.")[0];

    }
}

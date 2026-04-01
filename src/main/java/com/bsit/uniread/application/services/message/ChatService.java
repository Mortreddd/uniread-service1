package com.bsit.uniread.application.services.message;

import com.bsit.uniread.application.dto.request.message.NewMessageRequest;
import com.bsit.uniread.application.services.conversation.ConversationService;
import com.bsit.uniread.application.services.participant.ParticipantService;
import com.bsit.uniread.application.services.user.BlockUserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ConversationService conversationService;
    private final MessageService messageService;
    private final MessageBroadcaster broadcaster;
    private final ParticipantService participantService;
    private final BlockUserValidator blockUserValidator;


    @Transactional
    public void insertNewOneToOneConversationMessage(
            NewMessageRequest request,
            UUID conversationId,
            UUID senderId
    ) {
        validateConversationReader(conversationId, senderId);
        validateNewConversationMessage(request, conversationId, senderId);

        var message = messageService.createNewMessage(
                request,
                conversationId,
                senderId
        );

        var createdMessage = messageService.getMessageById(message.getId());
        var participants = participantService.getConversationParticipants(conversationId);
        var convo = conversationService.getConversationWithParticipantsMessage(conversationId);

        broadcaster.broadcastMessage(convo, createdMessage, participants);

    }

    public void markParticipantAsRead(UUID conversationId, UUID readerId) {
        validateConversationReader(conversationId, readerId);
        participantService.markParticipantAsRead(conversationId, readerId);
        broadcaster.broadcastNewParticipantReader(conversationId, readerId);
    }

    public void markParticipantAsTyping(UUID conversationId, UUID typerId, Boolean isTyping) {
        broadcaster.broadcastTypingIndicator(conversationId, typerId, isTyping);
    }


    private void validateNewConversationMessage(NewMessageRequest request, UUID conversationId, UUID senderId) {
        if(request == null || request.getContent().isBlank()) {
            log.warn("NewMessageRequest is null or message content is blank");
            throw new IllegalArgumentException("Message content is required");
        }
    }
    private void validateConversationReader(UUID conversationId, UUID readerId) {
        if(readerId == null || conversationId == null) {
            log.warn("Conversation {} or Reader {} are null", conversationId, readerId);
            throw new IllegalArgumentException(String.format("Conversation %s or Reader %s are null", conversationId, readerId));
        }
        if(!participantService.isParticipant(conversationId, readerId)) {
            log.warn("Participant {} does not belong in conversation {}", readerId, conversationId);
            throw new IllegalArgumentException(String.format("Participant %s does not belong in conversation %s", readerId, conversationId));
        }
    }
}

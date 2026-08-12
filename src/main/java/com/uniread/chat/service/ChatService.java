package com.uniread.chat.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.chat.domain.entities.Conversation;
import com.uniread.chat.domain.entities.Message;
import com.uniread.chat.domain.entities.Participant;
import com.uniread.chat.dto.request.NewMessageRequest;
import com.uniread.chat.dto.request.TypingParticipantRequest;
import com.uniread.chat.dto.response.ConversationDetailDto;
import com.uniread.chat.dto.response.ConversationPreviewDto;
import com.uniread.chat.dto.response.MessageDto;
import com.uniread.chat.mappers.ConversationMapper;
import com.uniread.user.dto.response.CurrentUser;
import com.uniread.user.service.BlockUserValidator;
import com.uniread.user.service.UserProfileService;
import com.uniread.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ConversationMapper conversationMapper;
    private final UserService userService;
    private final ConversationService conversationService;
    private final MessageService messageService;
    private final MessageBroadcaster broadcaster;
    private final ParticipantService participantService;
    private final BlockUserValidator blockUserValidator;

    @Transactional
    public void insertNewMessage(NewMessageRequest request, UUID conversationId, UUID senderId) {
        validateConversationReader(conversationId, senderId);
        validateNewConversationMessage(request, conversationId, senderId);
        var userDetails = userService.getCurrentUser(senderId);
        var message = messageService.createNewMessage(request, conversationId, userDetails);
        conversationService.changeConversationLastMessage(conversationId, message, userDetails.getProfile().getDisplayName(), userDetails.getId());

        var conversation = conversationService.getConversationWithParticipantsById(conversationId);
        var participants = conversation.getParticipants();

        var conversationPayload = conversationMapper.toPreviewDto(conversation, participants, userDetails.getId());
        broadcaster.broadcastToConversation(conversationPayload, message);
        broadcaster.broadcastToParticipants(conversationPayload, participants);
        markParticipantAsRead(conversationId, senderId);

    }

    public void markParticipantAsRead(UUID conversationId, UUID readerId) {
        validateConversationReader(conversationId, readerId);
        participantService.markParticipantAsRead(conversationId, readerId);
    }

    public void markParticipantAsTyping(UUID conversationId, UUID typerId, TypingParticipantRequest request) {
        broadcaster.broadcastTypingIndicator(conversationId, typerId, request.getUserAvatar(), request.getTyping());
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

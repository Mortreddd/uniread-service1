package com.bsit.uniread.application.services.conversation;

import com.bsit.uniread.application.dto.request.message.ConversationFilter;
import com.bsit.uniread.application.dto.response.message.ConversationPreviewDto;
import com.bsit.uniread.application.dto.response.message.ReaderParticipant;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.handler.exceptions.message.ConversationNotAllowedException;
import com.bsit.uniread.infrastructure.repositories.message.ConversationRepository;
import com.bsit.uniread.infrastructure.repositories.message.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final ParticipantRepository participantRepository;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Get the conversation for the user associated with
     * @param userId
     * @param filter
     * @return List of Conversation
     */
    @Transactional(readOnly = true)
    public Page<ConversationPreviewDto> getUserConversationsById(UUID userId, ConversationFilter filter) {
        Sort sort = Sort.by(Sort.Direction.DESC, "c.lastMessage.createdAt");
        Pageable pageable = PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort);
        return conversationRepository.findConversationsByParticipantId(userId, filter.getIsArchived(), pageable);
    }

    @Transactional
    public void markConversationAsReadAndNotify(UUID conversationId, UUID readerId) {
        Participant readerParticipant = participantRepository.findByConversationIdAndUserId(conversationId, readerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find user in conversation"));
        participantRepository.updateLastReadAtByConversationIdAndUserId(conversationId, readerId);

        messagingTemplate.convertAndSend("/topic/chat." + conversationId + ".read",
                new ReaderParticipant(readerParticipant.getId(), readerParticipant.getLastReadAt()));
    }

    @Transactional(readOnly = true)
    public Conversation getConversationById(UUID conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to retrieve the conversation"));
    }

    @Transactional(readOnly = true)
    public List<Participant> getConversationParticipants(Conversation conversation) {
        return participantRepository.findByConversation(conversation);
    }

    @Transactional
    public Conversation save(Conversation conversation) {
        return conversationRepository.save(conversation);
    }
}

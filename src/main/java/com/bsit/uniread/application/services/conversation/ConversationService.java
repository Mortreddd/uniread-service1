package com.bsit.uniread.application.services.conversation;

import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.handler.exceptions.message.ConversationNotAllowedException;
import com.bsit.uniread.infrastructure.repositories.message.ConversationRepository;
import com.bsit.uniread.infrastructure.repositories.message.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final ParticipantRepository participantRepository;
    private final UserService userService;

    /**
     * Get the conversation for the user associated with
     * @param userId
     * @return List of Conversation
     */
    @Transactional(readOnly = true)
    public Page<Conversation> getUserConversationsById(UUID userId, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        User user = userService.getUserById(userId);
        return conversationRepository.findByParticipantsUser(user, pageable);
    }

    /**
     * Get the conversation by id
     * @param userId
     * @param conversationId`
     * @return  conversation
     * @throws ConversationNotAllowedException
     */
    @Transactional(readOnly = true)
    public Conversation getConversationById(UUID userId, UUID conversationId) {
        if(!participantRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new ConversationNotAllowedException("Can't open selected Conversation");
        }
        return getConversationById(conversationId);
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

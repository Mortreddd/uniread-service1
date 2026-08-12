package com.uniread.chat.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.chat.dto.request.ConversationFilter;
import com.uniread.chat.dto.request.DirectConversationRequest;
import com.uniread.chat.dto.response.*;
import com.uniread.chat.domain.entities.Conversation;
import com.uniread.chat.mappers.ConversationMapper;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.chat.repositories.ConversationRepository;
import com.uniread.user.dto.response.UserDto;
import com.uniread.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationService {

    private final UserService userService;
    private final ConversationMapper conversationMapper;
    private final ConversationRepository conversationRepository;
    private final ParticipantService participantService;

    public Page<ConversationPreviewDto> getUserConversationsById(UUID userId, ConversationFilter filter) {
        Pageable pageable = PageRequest.of(
                filter.getPageNo(),
                filter.getPageSize(),
                Sort.by(Sort.Direction.DESC, "lastMessageAt")
        );

        return conversationRepository.findUserConversations(userId, pageable)
                .map(convo -> conversationMapper.toPreviewDto(convo, userId));
    }

    public Conversation getConversationById(UUID conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to retrieve the conversation"));
    }

    public Conversation getConversationWithParticipantsById(UUID conversationId) {
        return conversationRepository.findWithParticipantsById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to retrieve the conversation"));
    }

    public ConversationPreviewDto getConversationPreviewById(UUID conversationId, CustomUserDetails userDetails) {
        return getConversationPreviewById(conversationId, userDetails.getId());
    }
    public ConversationPreviewDto getConversationPreviewById(UUID conversationId, UUID userId) {
        var conversation = getConversationWithParticipantsById(conversationId);
        return conversationMapper.toPreviewDto(conversation, userId);

    }

    @Transactional
    public ConversationDto getOrCreateDirectConversation(
            CustomUserDetails userDetails,
            DirectConversationRequest request
    ) {
        var creator = userService.getUserById(userDetails.getId());
        var recipient = userService.getUserById(request.getReceiverId());
        var conversation = conversationRepository.findDirectConversation(creator.getId(), recipient.getId())
                .orElseGet(() -> createDirectConversation(creator, recipient));

        return conversationMapper.toDto(conversation);
    }


    @Transactional
    public Conversation createDirectConversation(UserDto creator, UserDto recipient) {
        var conv = conversationRepository.save(Conversation.builder().isGroup(false).build());

        participantService.createParticipant(conv.getId(), creator);
        participantService.createParticipant(conv.getId(), recipient);

        log.debug("Conversation {} has been created", conv.getId());
        return conv;
    }

    @Transactional
    public void markDeleteConversation(UUID conversationId, CustomUserDetails userDetails) {
        participantService.markDeleted(conversationId, userDetails);

    }

    @Transactional
    public void changeConversationLastMessage(
            UUID conversationId,
            MessageDto message,
            String senderName,
            UUID senderId
    ) {
        conversationRepository.updateConversationLastMessage(
                conversationId,
                message.getId(),
                message.getMessage(),
                message.getDeliveredAt(),
                senderName,
                senderId
        );
    }

}
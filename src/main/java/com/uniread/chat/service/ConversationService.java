package com.uniread.chat.service;

import com.uniread.chat.dto.request.ConversationFilter;
import com.uniread.chat.dto.request.ExistingConversationFilter;
import com.uniread.chat.dto.response.ConversationDetailDto;
import com.uniread.chat.dto.response.ConversationInfo;
import com.uniread.chat.dto.response.ConversationPreviewDto;
import com.uniread.chat.domain.entities.Conversation;
import com.uniread.chat.domain.entities.ParticipantRole;
import com.uniread.chat.mappers.ConversationMapper;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.chat.repositories.ConversationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationMapper conversationMapper;
    private final ConversationRepository conversationRepository;
    private final ParticipantService participantService;

    public Page<ConversationPreviewDto> getUserConversationsById(UUID userId, ConversationFilter filter) {
        Sort sort = Sort.by(Sort.Direction.DESC, "c.lastMessage.createdAt");
        Pageable pageable = PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort);
        return conversationRepository.findConversationsByParticipantId(userId, filter.getIsArchived(), pageable);
    }

    @Transactional
    public Conversation createGroupChat(String name, UUID creatorId, List<UUID> memberIds) {

        var conversation = conversationRepository.save(
                Conversation.builder()
                .isGroup(true)
                .avatarPhoto(null)
                .name(name)
                .build()
        );

        participantService.createParticipant(conversation.getId(), creatorId, ParticipantRole.OWNER);
        participantService.addParticipants(conversation.getId(), memberIds);


        log.trace("New created conversation {} with name of {}", conversation.getId(), conversation.getName());

        return conversation;

    }


    public ConversationDetailDto getConversationWithParticipantsMessage(UUID conversationId, UUID senderId) {
        var convo = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to retrieve the preview for conversation"));

        return conversationMapper.toDetailDto(convo, senderId);
    }

    public ConversationDetailDto getConversationById(UUID conversationId, UUID receiverId) {
        return conversationRepository.findUserConversationById(conversationId, receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to retrieve the conversation"));
    }

    @Transactional(readOnly = true)
    public ConversationInfo getOneToOneConversation(UUID receiverId, ExistingConversationFilter filter, UUID currentUserId) {
        Conversation conversation = conversationRepository.findOneOnOneConversation(currentUserId, receiverId, filter.getIsGroup())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation with user does not exist"));

        return new ConversationInfo(conversation.getId());
    }



    @Transactional
    public Conversation createGroupConversation(UUID creatorId, List<UUID> memberIds) {
        var convo = conversationRepository.save(Conversation.builder().isGroup(true).build());


        participantService.createParticipant(convo.getId(), creatorId, ParticipantRole.ADMIN);
        participantService.addParticipants(convo.getId(), memberIds);

        return convo;
    }

    @Transactional
    public Conversation createOneToOneConversation(UUID creatorId, UUID receiverId) {
        var conv = conversationRepository.save(Conversation.builder().isGroup(false).build());


        participantService.createParticipant(conv.getId(), creatorId);
        participantService.createParticipant(conv.getId(), receiverId);

        log.debug("Conversation {} has been created", conv.getId());
        return conv;
    }

}

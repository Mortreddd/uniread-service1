package com.bsit.uniread.application.services.conversation;

import com.bsit.uniread.application.dto.request.message.ConversationFilter;
import com.bsit.uniread.application.dto.request.message.ExistingConversationFilter;
import com.bsit.uniread.application.dto.response.message.ConversationDetailDto;
import com.bsit.uniread.application.dto.response.message.ConversationInfo;
import com.bsit.uniread.application.dto.response.message.ConversationPreviewDto;
import com.bsit.uniread.application.services.participant.ParticipantService;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.ParticipantRole;
import com.bsit.uniread.domain.mappers.message.ConversationMapper;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.message.ConversationRepository;
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


    public ConversationDetailDto getConversationWithParticipantsMessage(UUID conversationId) {
        var convo = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to retrieve the preview for conversation"));

        return conversationMapper.toDetailDto(convo);
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

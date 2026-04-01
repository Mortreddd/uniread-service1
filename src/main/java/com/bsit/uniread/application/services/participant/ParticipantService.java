package com.bsit.uniread.application.services.participant;

import com.bsit.uniread.application.dto.response.message.ParticipantDto;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.message.ParticipantRole;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.mappers.message.ParticipantMapper;
import com.bsit.uniread.infrastructure.handler.exceptions.DuplicateResourceException;
import com.bsit.uniread.infrastructure.repositories.message.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantService {

    private final ParticipantMapper participantMapper;
    private final ParticipantRepository participantRepository;

    @Transactional
    public void markParticipantAsRead(UUID conversationId, UUID userId) {
        int rows = participantRepository.updateLastReadAtByConversationIdAndUserId(conversationId, userId);
        log.debug("Participant mark as read are {}", rows);
    }

    public List<ParticipantDto> getConversationParticipants(UUID conversationId) {
        return participantRepository.findByConversationId(conversationId)
                .stream()
                .map(participantMapper::toDto)
                .toList();
    }

    public Participant createParticipant(UUID conversationId, UUID userId) {
        return createParticipant(conversationId, userId, ParticipantRole.MEMBER);
    }

    @Transactional
    public Participant createParticipant(UUID conversationId, UUID userId, ParticipantRole role) {
        log.info("Adding user {} in conversation {}", userId, conversationId);

        if(isParticipant(conversationId, userId)) {
           throw new DuplicateResourceException(String.format("User %s is already in conversation %s", userId, conversationId));
        }

        var convo = Conversation.builder().id(conversationId).build();
        var user = User.builder().id(userId).build();

        var participant = Participant.builder()
                .conversation(convo)
                .user(user)
                .role(role)
                .lastReadAt(Instant.now())
                .archived(false)
                .muted(false)
                .mutedUntil(null)
                .joinedAt(Instant.now())
                .build();

        var saved = participantRepository.save(participant);
        log.info("User {} has been added in conversation {} with role {}", userId, conversationId, role);
        return saved;
    }

    public Participant cretaeParticipant(UUID conversationId, UUID userId) {
        return createParticipant(conversationId, userId, ParticipantRole.MEMBER);
    }

    @Transactional
    public void addParticipants(UUID conversationId, List<UUID> memberIds) {
        validateExistingMembers(conversationId, memberIds);

        var convo = Conversation.builder().id(conversationId).build();
        var participants = memberIds.stream()
                .map(memberId -> Participant.builder()
                        .user(User.builder().id(memberId).build())
                        .conversation(convo)
                        .joinedAt(Instant.now())
                        .lastReadAt(Instant.now())
                        .archived(false)
                        .muted(false)
                        .mutedUntil(null)
                        .build())
                .toList();


        participantRepository.saveAll(participants);
    }

    public void validateExistingMembers(UUID conversationId, List<UUID> memberIds) {
        if(participantRepository.existsByConversationIdAndUserIdIn(conversationId, memberIds)) {
            throw new IllegalArgumentException("Some members are already in the conversation");
        }
    }


    @Transactional
    public void removeUserFromAllConversations(UUID userId) {
        int deleted = participantRepository.deleteByUserId(userId);
        log.info("User {} removed from {} conversations", userId, deleted);
    }

    public Boolean isParticipant(UUID conversationId, UUID userId) {
        return participantRepository.existsByConversationIdAndUserId(conversationId, userId);
    }

}

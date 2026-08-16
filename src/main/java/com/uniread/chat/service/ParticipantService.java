package com.uniread.chat.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.chat.domain.entities.Conversation;
import com.uniread.chat.domain.entities.Participant;
import com.uniread.chat.domain.entities.ParticipantRole;
import com.uniread.chat.mappers.ParticipantMapper;
import com.uniread.common.exceptions.DuplicateResourceException;
import com.uniread.chat.repositories.ParticipantRepository;
import com.uniread.auth.domain.entities.User;
import com.uniread.user.dto.response.UserDto;
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
    }

    @Transactional
    public void createParticipant(UUID conversationId, UserDto user) {
        createParticipant(conversationId, user, ParticipantRole.MEMBER);
    }

    @Transactional
    public void createParticipant(UUID conversationId, UserDto user, ParticipantRole role) {
        log.info("Adding user {} in conversation {}", user.getId(), conversationId);

        if(isParticipant(conversationId, user.getId())) {
           throw new DuplicateResourceException(String.format("User %s is already in conversation %s", user.getId(), conversationId));
        }

        var convo = Conversation.builder().id(conversationId).build();

        var participant = Participant.builder()
                .conversation(convo)
                .user(User.builder().id(user.getId()).build())
                .nickname(user.getProfile().getDisplayName())
                .role(role)
                .lastReadAt(Instant.now())
                .archived(false)
                .muted(false)
                .mutedUntil(null)
                .joinedAt(Instant.now())
                .build();

        var saved = participantRepository.save(participant);
        log.info("User {} has been added in conversation {} with role {}", user.getId(), conversationId, role);
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

    @Transactional
    public void markDeleted(UUID conversationId, CustomUserDetails userDetails) {
        validateMember(conversationId, userDetails.getId());

        participantRepository.updateDeletedByConversationIdAndParticipantUserId(conversationId, userDetails.getId());
    }

    public void validateMember(UUID conversationId, UUID memberId) {
        if(!participantRepository.existsByConversationIdAndUserId(conversationId, memberId)) {
            throw new IllegalArgumentException("You're not belong to the conversation");
        }
    }
    public void validateExistingMembers(UUID conversationId, List<UUID> memberIds) {
        if(participantRepository.existsByConversationIdAndUserIdIn(conversationId, memberIds)) {
            throw new IllegalArgumentException("Some members are already in the conversation");
        }
    }

    public Boolean isParticipant(UUID conversationId, UUID userId) {
        return participantRepository.existsByConversationIdAndUserId(conversationId, userId);
    }

}

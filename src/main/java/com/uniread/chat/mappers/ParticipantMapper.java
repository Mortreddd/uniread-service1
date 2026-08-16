package com.uniread.chat.mappers;

import com.uniread.chat.dto.response.ParticipantDto;
import com.uniread.chat.domain.entities.Participant;
import com.uniread.auth.domain.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ParticipantMapper {

    public ParticipantDto toDto(Participant participant) {
        return Optional.ofNullable(participant)
                .map(this::mapParticipantToDto)
                .orElse(null);
    }

    private ParticipantDto mapParticipantToDto(Participant participant) {
        return ParticipantDto.builder()
                .id(participant.getId())
                .conversationId(participant.getConversation() != null ? participant.getConversation().getId() : null)
                .userId(participant.getUser() != null ? participant.getUser().getId() : null)
                .role(participant.getRole())
                .nickname(participant.getNickname())
                .fullName(getFullName(participant.getUser()))
                .unreadCount(participant.getUnreadCount())
                .muted(participant.getMuted())
                .mutedUntil(participant.getMutedUntil())
                .joinedAt(participant.getJoinedAt())
                .leftAt(participant.getLeftAt())
                .archived(participant.getArchived())
                .lastReadAt(participant.getLastReadAt())
                .addedAt(participant.getCreatedAt())
                .build();
    }

    public List<ParticipantDto> toDtoList(List<Participant> participants) {
        return Optional.ofNullable(participants)
                .map(list -> list.stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    private String getFullName(User user) {
        return Optional.ofNullable(user)
                .map(User::getProfile)
                .map(profile -> {
                    String firstName = Optional.ofNullable(profile.getFirstName()).orElse("");
                    String lastName = Optional.ofNullable(profile.getLastName()).orElse("");
                    String fullName = (firstName + " " + lastName).trim();
                    return fullName.isEmpty() ? user.getUsername() : fullName;
                })
                .orElseGet(() ->
                        Optional.ofNullable(user)
                                .map(User::getUsername)
                                .orElse("Unknown User")
                );
    }
}
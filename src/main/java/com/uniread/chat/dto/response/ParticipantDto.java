package com.uniread.chat.dto.response;

import com.uniread.chat.domain.entities.ParticipantRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class ParticipantDto {
    private UUID id;
    private UUID conversationId;
    private UUID userId;
    private ParticipantRole role;
    private String nickname;
    private String fullName;
    private Long unreadCount;
    private Boolean muted;
    private Instant mutedUntil;
    private Instant joinedAt;
    private Instant leftAt;
    private Boolean archived;
    private Instant lastReadAt;
    private Instant addedAt;
}

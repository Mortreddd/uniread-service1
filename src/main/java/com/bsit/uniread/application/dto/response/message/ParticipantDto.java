package com.bsit.uniread.application.dto.response.message;

import com.bsit.uniread.application.dto.response.user.SimpleUserInfo;
import com.bsit.uniread.domain.entities.message.ParticipantRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
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

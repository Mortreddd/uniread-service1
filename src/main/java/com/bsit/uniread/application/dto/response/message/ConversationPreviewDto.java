package com.bsit.uniread.application.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ConversationPreviewDto {

    private UUID conversationId;
    private String name;
    private String avatar;

    private Long unreadCount;
    private Boolean hasNewMessage;
    private Boolean isMuted;
    private Boolean isArchived;
    private Boolean isGroup;

    private Instant lastMessageAt;

}

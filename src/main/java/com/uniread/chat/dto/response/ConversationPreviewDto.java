package com.uniread.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ConversationPreviewDto {

    private UUID conversationId;
    private String name;
    private String avatarUrl;

    private Long unreadCount;
    private Boolean hasNewMessage;
    private Boolean isMuted;
    private Boolean isArchived;
    private Boolean isGroup;

    private String lastMessageText;
    private Instant lastMessageAt;
    private String lastSenderName;
    private UUID lastSenderId;
}

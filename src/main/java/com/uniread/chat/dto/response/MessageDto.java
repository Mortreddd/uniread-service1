package com.uniread.chat.dto.response;

import com.uniread.chat.domain.entities.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MessageDto {

    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String senderName;
    private String senderPhoto;
    private MessageType type;
    private String message;
    private Instant deliveredAt;
    private Instant createdAt;
}

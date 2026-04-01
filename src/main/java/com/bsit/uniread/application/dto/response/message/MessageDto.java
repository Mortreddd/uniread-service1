package com.bsit.uniread.application.dto.response.message;

import com.bsit.uniread.domain.entities.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MessageDto {

    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String senderName;
    private MessageType type;
    private String message;
    private Instant deliveredAt;
    private Instant createdAt;
}

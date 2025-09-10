package com.bsit.uniread.application.dto.response.message;

import com.bsit.uniread.application.dto.response.user.SimpleUserInfo;
import com.bsit.uniread.domain.entities.message.Message;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MessageDto {

    private UUID id;
    private UUID conversationId;
    private SimpleUserInfo sender;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.conversationId = message.getConversation().getId();
        this.sender = new SimpleUserInfo(message.getSender());
        this.message = message.getMessage();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
    }


}

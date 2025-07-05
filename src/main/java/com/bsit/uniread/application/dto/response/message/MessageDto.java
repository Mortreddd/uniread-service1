package com.bsit.uniread.application.dto.response.message;

import com.bsit.uniread.application.dto.response.user.UserChatInfo;
import com.bsit.uniread.domain.entities.message.Message;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MessageDto {

    private UUID id;
    private UUID conversationId;
    private UserChatInfo sender;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.conversationId = message.getConversation().getId();
        this.sender = new UserChatInfo(message.getSender());
        this.message = message.getMessage();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
    }


}

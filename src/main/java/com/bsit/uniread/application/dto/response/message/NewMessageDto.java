package com.bsit.uniread.application.dto.response.message;

import com.bsit.uniread.application.dto.response.user.SimpleUserInfo;
import com.bsit.uniread.domain.entities.message.Message;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class NewMessageDto {

    private final UUID id;
    private final ConversationPreviewDto conversationPreview;
    private final SimpleUserInfo sender;
    private final String message;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public NewMessageDto(ConversationPreviewDto conversationPreview, Message message) {
        this.id = message.getId();
        this.conversationPreview = conversationPreview;
        this.sender = new SimpleUserInfo(message.getSender());
        this.message = message.getMessage();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
    }
}

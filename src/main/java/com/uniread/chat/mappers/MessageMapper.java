package com.uniread.chat.mappers;

import com.uniread.chat.dto.response.MessageDto;
import com.uniread.chat.domain.entities.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageDto toDto(Message message) {
        if (message == null) return null;

        return MessageDto.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSenderName())
                .senderPhoto(message.getSenderPhoto())
                .type(message.getMessageType())
                .message(message.getMessage())
                .deliveredAt(message.getDeliveredAt())
                .createdAt(message.getCreatedAt())
                .build();
    }

}
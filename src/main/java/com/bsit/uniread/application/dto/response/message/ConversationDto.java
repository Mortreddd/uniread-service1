package com.bsit.uniread.application.dto.response.message;

import com.bsit.uniread.domain.entities.message.Conversation;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class ConversationDto {

    private UUID id;
    private String name;
    private List<ParticipantDto> participants;
    private List<MessageDto> messages;
    private LocalDateTime createdAt;
    private Boolean isGroup;

    public ConversationDto(Conversation conversation) {
        this.id = conversation.getId();
        this.name = conversation.getName();
        this.participants = conversation.getParticipants()
                .stream()
                .map(ParticipantDto::new)
                .toList();
        this.messages = conversation
                .getMessages()
                .stream()
                .map(MessageDto::new)
                .toList();

        this.createdAt = conversation.getCreatedAt();
        this.isGroup = conversation.getIsGroup();
    }
}

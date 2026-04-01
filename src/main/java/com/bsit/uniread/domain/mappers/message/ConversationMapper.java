package com.bsit.uniread.domain.mappers.message;


import com.bsit.uniread.application.dto.response.message.ConversationDetailDto;
import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.domain.entities.message.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "avatar", source = "conversation.avatarPhoto")
    @Mapping(target = "isMuted", expression = "java(getMutedStatus(conversation, currentUserId))")
    @Mapping(target = "isArchived", expression = "java(getArchivedStatus(conversation, currentUserId))")
    @Mapping(target = "lastMessage", source = "conversation.lastMessage")
    ConversationDetailDto toDetailDto(Conversation conversation);

    // MapStruct will automatically use this to map the nested lastMessage!
    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderName", expression = "java(message.getSender().getProfile().getFirstName() + \" \" + message.getSender().getProfile().getLastName())")
    MessageDto toMessageDto(Message message);

    // Custom helper to pull the requester's muted status safely
    default Boolean getMutedStatus(Conversation conversation, UUID currentUserId) {
        return conversation.getParticipants().stream()
                .filter(p -> p.getUser().getId().equals(currentUserId))
                .map(Participant::getMuted) // Assuming your Participant entity has getMuted()
                .findFirst()
                .orElse(false);
    }

    // Custom helper to pull the requester's archived status safely
    default Boolean getArchivedStatus(Conversation conversation, UUID currentUserId) {
        return conversation.getParticipants().stream()
                .filter(p -> p.getUser().getId().equals(currentUserId))
                .map(Participant::getArchived)
                .findFirst()
                .orElse(false);
    }
}

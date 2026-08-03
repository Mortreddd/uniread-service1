package com.uniread.chat.mappers;


import com.uniread.chat.dto.response.ConversationDetailDto;
import com.uniread.chat.dto.response.ConversationDto;
import com.uniread.chat.dto.response.MessageDto;
import com.uniread.chat.domain.entities.Conversation;
import com.uniread.chat.domain.entities.Message;
import com.uniread.chat.domain.entities.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConversationMapper {

    private final MessageMapper messageMapper;

    public ConversationDto toDto(Conversation conversation) {
        return ConversationDto.builder()
                .id(conversation.getId())
                .name(conversation.getName())
                .avatarPhoto(conversation.getAvatarPhoto())
                .isGroup(conversation.getIsGroup())
                .build();
    }

    public ConversationDetailDto toDetailDto(Conversation conversation, UUID currentUserId) {
        if (conversation == null) {
            return null;
        }
        return ConversationDetailDto.builder()
                .conversationId(conversation.getId())
                .name(conversation.getName())
                .avatar(conversation.getAvatarPhoto())
                .isGroup(conversation.getIsGroup())
                .isMuted(getMutedStatus(conversation, currentUserId))
                .isArchived(getArchivedStatus(conversation, currentUserId))
                .lastMessage(mapLastMessage(conversation.getLastMessage()))
                .build();
    }

    private MessageDto mapLastMessage(Message lastMessage) {
        if (lastMessage == null) {
            return null;
        }
        return messageMapper.toMessageDto(lastMessage);
    }

    private Boolean getMutedStatus(Conversation conversation, UUID currentUserId) {
        if (conversation == null || currentUserId == null || conversation.getParticipants() == null) {
            return false;
        }

        return conversation.getParticipants().stream()
                .filter(p -> p.getUser() != null && p.getUser().getId().equals(currentUserId))
                .map(Participant::getMuted)
                .findFirst()
                .orElse(false);
    }

    private Boolean getArchivedStatus(Conversation conversation, UUID currentUserId) {
        return conversation.getParticipants().stream()
                .filter(p -> p.getUser().getId().equals(currentUserId))
                .map(Participant::getArchived)
                .findFirst()
                .orElse(false);
    }
}

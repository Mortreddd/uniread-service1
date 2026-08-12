package com.uniread.chat.mappers;


import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.chat.dto.response.ConversationDetailDto;
import com.uniread.chat.dto.response.ConversationDto;
import com.uniread.chat.dto.response.ConversationPreviewDto;
import com.uniread.chat.dto.response.MessageDto;
import com.uniread.chat.domain.entities.Conversation;
import com.uniread.chat.domain.entities.Message;
import com.uniread.chat.domain.entities.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConversationMapper {

    private final MessageMapper messageMapper;

    public ConversationPreviewDto toPreviewDto(
            Conversation conversation,
            UUID currentUserId
    ) {
        var participants = conversation.getParticipants();
        return toPreviewDto(conversation, participants, currentUserId);
    }
    public ConversationPreviewDto toPreviewDto(
            Conversation conversation,
            List<Participant> participants,
            UUID currentUserId
    ) {

        if(conversation == null) return null;

        var currentParticipant = participants.stream()
                .filter(p -> p.getUser().getId().equals(currentUserId))
                .findFirst()
                .orElse(null);

        var otherParticipant = participants.stream()
                .filter(p -> !p.getUser().getId().equals(currentUserId))
                .findFirst()
                .orElse(null);

        String conversationName = conversation.getIsGroup()
                ? conversation.getName()
                : (otherParticipant != null
                ? otherParticipant.getUser().getProfile().getDisplayName()
                : null);

        String conversationAvatar = conversation.getIsGroup()
                ? conversation.getAvatarPhoto()
                : (otherParticipant != null
                ? otherParticipant.getUser().getProfile().getAvatarUrl()
                : null);

        String conversationAvatarPublicId = conversation.getIsGroup()
                ? conversation.getAvatarPublicId()
                : (otherParticipant != null
                ? otherParticipant.getUser().getProfile().getAvatarPublicId()
                : null);

        return ConversationPreviewDto.builder()
                .conversationId(conversation.getId())
                .name(conversationName)
                .avatarUrl(conversationAvatar)
                .avatarPublicId(conversationAvatarPublicId)
                .unreadCount(currentParticipant != null ? currentParticipant.getUnreadCount() : 0L)
                .hasNewMessage(currentParticipant != null && currentParticipant.getUnreadCount() > 0)
                .isMuted(currentParticipant != null && currentParticipant.getMuted())
                .isArchived(currentParticipant != null && currentParticipant.getArchived())
                .isGroup(conversation.getIsGroup())
                .lastMessageText(conversation.getLastMessageText())
                .lastMessageAt(conversation.getLastMessageAt())
                .lastSenderName(conversation.getLastSenderName())
                .lastSenderId(conversation.getLastSenderId())
                .build();
    }

    public ConversationDto toDto(Conversation conversation) {
        return ConversationDto.builder()
                .id(conversation.getId())
                .name(conversation.getName())
                .avatarPhoto(conversation.getAvatarPhoto())
                .isGroup(conversation.getIsGroup())
                .build();
    }
}

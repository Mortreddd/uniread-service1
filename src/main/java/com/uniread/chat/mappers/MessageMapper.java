package com.uniread.chat.mappers;

import com.uniread.chat.domain.entities.Conversation;
import com.uniread.chat.dto.response.MessageDto;
import com.uniread.chat.domain.entities.Message;
import com.uniread.user.domain.entities.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageMapper {

    public MessageDto toMessageDto(Message message) {
        if (message == null) {
            return null;
        }

        return new MessageDto(
                message.getId(),
                message.getConversation() != null ? message.getConversation().getId() : null,
                message.getSender() != null ? message.getSender().getId() : null,
                getSenderFullName(message.getSender()),
                message.getMessageType(),
                message.getMessage(),
                message.getDeliveredAt(),
                message.getCreatedAt()
        );
    }

    private MessageDto mapMessageToDto(Message message) {
        return new MessageDto(
                message.getId(),
                Optional.ofNullable(message.getConversation())
                        .map(Conversation::getId)
                        .orElse(null),
                Optional.ofNullable(message.getSender())
                        .map(User::getId)
                        .orElse(null),
                getSenderFullName(message.getSender()),
                message.getMessageType(),
                message.getMessage(),
                message.getDeliveredAt(),
                message.getCreatedAt()
        );
    }

    public String getSenderFullName(User sender) {
        return Optional.ofNullable(sender)
                .map(User::getProfile)
                .map(profile -> {
                    String firstName = Optional.ofNullable(profile.getFirstName()).orElse("");
                    String lastName = Optional.ofNullable(profile.getLastName()).orElse("");
                    String fullName = (firstName + " " + lastName).trim();
                    return fullName.isEmpty() ? sender.getUsername() : fullName;
                })
                .orElseGet(() ->
                        Optional.ofNullable(sender)
                                .map(User::getUsername)
                                .orElse("Unknown User")
                );
    }
}
package com.uniread.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class ConversationDetailDto {
    private UUID conversationId;
    private String name;
    private String avatar;

    private Boolean isMuted;
    private Boolean isArchived;
    private Boolean isGroup;
    private MessageDto lastMessage;
    // TODO: Extend this object for including of settings, options of conversation

}

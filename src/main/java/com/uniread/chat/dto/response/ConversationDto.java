package com.uniread.chat.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class ConversationDto {
    private UUID id;

    private String name;
    private String avatarPhoto;

    private Boolean isGroup;
}

package com.uniread.chat.dto.request;

import com.uniread.chat.domain.entities.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewMessageRequest {
    private UUID conversationId;
    private MessageType messageType;
    private String content;
}

package com.uniread.chat.dto.request;

import com.uniread.chat.domain.entities.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewMessageRequest {
    private MessageType messageType;
    private String content;
}

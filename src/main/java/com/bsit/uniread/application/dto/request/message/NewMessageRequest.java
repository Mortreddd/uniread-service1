package com.bsit.uniread.application.dto.request.message;

import com.bsit.uniread.domain.entities.message.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class NewMessageRequest {
    private MessageType messageType;
    private String content;
}

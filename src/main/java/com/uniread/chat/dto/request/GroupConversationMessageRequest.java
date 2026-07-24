package com.uniread.chat.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupConversationMessageRequest {
    private UUID conversationId;
    private String message;
}

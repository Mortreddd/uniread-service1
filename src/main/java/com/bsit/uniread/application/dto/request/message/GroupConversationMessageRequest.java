package com.bsit.uniread.application.dto.request.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupConversationMessageRequest {
    private UUID conversationId;
    private String message;
}

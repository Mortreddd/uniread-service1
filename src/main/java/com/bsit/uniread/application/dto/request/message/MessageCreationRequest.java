package com.bsit.uniread.application.dto.request.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageCreationRequest {
    private UUID conversationId;
    private UUID senderId;
    private String message;
}

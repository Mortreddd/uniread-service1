package com.bsit.uniread.application.dto.request.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageCreationRequest {
    private UUID conversationId;
    private List<UUID> receiverIds;
    private Boolean isGroup;
    private String message;
}

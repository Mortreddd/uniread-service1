package com.bsit.uniread.application.dto.request.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StartConversationRequest {

    private String conversationName;

    @NotNull(message = "Sender must not be null")
    private UUID senderId;
    @NotNull(message = "Receivers must not be null")
    private UUID[] receiverIds;
    @NotNull(message = "Message must not be null")
    private String message;

    private Boolean isGroup;

}

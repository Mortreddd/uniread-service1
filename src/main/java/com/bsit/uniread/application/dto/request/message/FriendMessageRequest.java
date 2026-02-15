package com.bsit.uniread.application.dto.request.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class FriendMessageRequest {
    private UUID receiverId;
    private String message;
}

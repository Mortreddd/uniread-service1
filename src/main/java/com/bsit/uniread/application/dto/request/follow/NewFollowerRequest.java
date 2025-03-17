package com.bsit.uniread.application.dto.request.follow;

import lombok.Data;

import java.util.UUID;

@Data
public class NewFollowerRequest {
    private UUID requesterId;
}

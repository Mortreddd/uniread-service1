package com.uniread.social.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class NewFollowerRequest {
    private UUID requesterId;
}

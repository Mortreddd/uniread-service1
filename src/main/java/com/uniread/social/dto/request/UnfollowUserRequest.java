package com.uniread.social.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UnfollowUserRequest {

    @NotNull
    private UUID requesterId;
}

package com.bsit.uniread.application.dto.request.follow;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UnfollowUserRequest {

    @NotNull
    private UUID requesterId;
}

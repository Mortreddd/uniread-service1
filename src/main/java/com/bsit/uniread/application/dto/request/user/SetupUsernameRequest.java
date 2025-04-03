package com.bsit.uniread.application.dto.request.user;

import com.bsit.uniread.infrastructure.security.validations.constraints.UniqueUsername;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SetupUsernameRequest {

    @NotNull
    @UniqueUsername
    private String username;
}

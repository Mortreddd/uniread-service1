package com.uniread.user.dto.request;

import com.uniread.auth.security.validations.constraints.UniqueUsername;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SetupUsernameRequest {

    @NotNull
    @UniqueUsername
    private String username;
}

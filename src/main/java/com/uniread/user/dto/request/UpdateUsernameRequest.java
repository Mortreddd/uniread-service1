package com.uniread.user.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUsernameRequest {
    @Pattern(
        regexp = "^[a-zA-Z0-9._-]+$",
        message = "Username is invalid"
    )
    @Size(min = 6, max = 20, message = "Username must be between 6 and 15 characters")
    private String username;
}

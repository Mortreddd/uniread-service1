package com.uniread.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VerifyEmailRequest {
    @NotBlank(message = "Verification token is empty")
    private String token;
}
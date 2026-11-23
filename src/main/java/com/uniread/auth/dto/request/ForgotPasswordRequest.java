package com.uniread.auth.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
final public class ForgotPasswordRequest {

    @NotBlank
    private String username;

}

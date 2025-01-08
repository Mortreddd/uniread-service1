package com.bsit.uniread.application.dto.request.auth;

import com.bsit.uniread.infrastructure.security.validations.constants.ValidationMessagesConstant;
import com.bsit.uniread.infrastructure.security.validations.constraints.EmailExists;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {

    @Email(message = ValidationMessagesConstant.EMAIL)
    @EmailExists
    private String email;

    @NotNull(message = ValidationMessagesConstant.PASSWORD)
    private String password;

}

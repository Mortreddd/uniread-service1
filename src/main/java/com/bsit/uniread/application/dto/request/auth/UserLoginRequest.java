package com.bsit.uniread.application.dto.request.auth;

import com.bsit.uniread.infrastructure.security.validations.constants.ValidationMessagesConstant;
import com.bsit.uniread.infrastructure.security.validations.constraints.EmailExists;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {

    @Email(message = ValidationMessagesConstant.EMAIL)
    @EmailExists
    private String email;

    @Min(value = 8, message = ValidationMessagesConstant.PASSWORD_LENGTH)
    private String password;

}

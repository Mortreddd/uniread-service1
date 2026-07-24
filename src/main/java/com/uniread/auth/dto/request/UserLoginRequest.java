package com.uniread.auth.dto.request;

import com.uniread.auth.security.validations.constants.ValidationMessagesConstant;
import com.uniread.auth.security.validations.constraints.EmailExists;
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

package com.uniread.auth.dto.request;


import com.uniread.auth.security.validations.constraints.EmailExists;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
final public class ForgotPasswordRequest {

    @Email(message = "Invalid email")
    @EmailExists
    private String email;

}

package com.bsit.uniread.application.dto.request.auth;


import com.bsit.uniread.infrastructure.security.validations.constraints.EmailExists;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
final public class ForgotPasswordRequest {

    @Email(message = "Invalid email")
    @EmailExists
    private String email;

}

package com.bsit.uniread.application.dto.request.auth;

import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.infrastructure.security.validations.constraints.UniqueEmail;
import com.bsit.uniread.infrastructure.security.validations.constraints.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegistrationRequest {

    @NotNull(message = "First name is required")
    private String firstName;
    @NotNull(message = "Last name is required")
    private String lastName;

    @UniqueUsername
    private String username;
    @NotNull(message = "Gender is required")
    private Gender gender;

    @Email(message = "Email must be valid")
    @UniqueEmail
    private String email;

    @Min(value = 8, message = "Password must be 8 characters long or above")
    private String password;


}

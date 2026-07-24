package com.uniread.auth.dto.request;

import com.uniread.user.domain.entities.Gender;
import com.uniread.auth.security.validations.constraints.UniqueUsername;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserRegistrationRequest {
    @NotNull(message = "First name is required")
    @Pattern(regexp = "^[a-zA-Z]{3,60}$", message = "First Name must be alphabetic and 3-60 characters long")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Pattern(regexp = "^[a-zA-Z]{3,60}$", message = "Last Name must be alphabetic and 3-60 characters long")
    private String lastName;

    @Pattern(
        regexp = "^[a-zA-Z0-9._-]+$",
        message = "Username is invalid"
    )
    @Size(min = 6, max = 20, message = "Username must be between 6 and 15 characters")
    private String username;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email field is invalid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
        regexp = ".*[A-Z].*",
        message = "Password must contain at least one uppercase letter"
    )
    @Pattern(
        regexp = ".*\\d.*",
        message = "Password must contain at least one digit"
    )
    @Pattern(
        regexp = ".*[^a-zA-Z0-9].*",
        message = "Password must contain at least one special character"
    )
    private String password;

    @NotBlank(message = "Confirm Password is required")
    @Size(min = 8, message = "Confirm Password must be at least 8 characters long")
    @Pattern(
            regexp = ".*[A-Z].*",
            message = "Confirm Password must contain at least one uppercase letter"
    )
    @Pattern(
            regexp = ".*\\d.*",
            message = "Confirm Password must contain at least one digit"
    )
    @Pattern(
            regexp = ".*[^a-zA-Z0-9].*",
            message = "Confirm Password must contain at least one special character"
    )
    private String confirmPassword;
}

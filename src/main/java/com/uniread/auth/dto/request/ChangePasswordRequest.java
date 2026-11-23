package com.uniread.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {

    @NotBlank
    private String token;

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

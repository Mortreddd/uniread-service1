package com.uniread.user.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.uniread.user.domain.entities.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserProfileRequest {

    @NotBlank(message = "Display name is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._\\- ]+$",
            message = "Display name should not contain illegal characters"
    )
    @Size(max = 50, message = "Display name must be at most 50 characters")
    private String displayName;

    @NotBlank(message = "First name is required")
    @Pattern(
            regexp = "^\\p{L}+(['-]\\p{L}+)*$",
            message = "First name contains invalid characters"
    )
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(
            regexp = "^[\\p{L}]+(['-][\\p{L}]+)*$",
            message = "Last name contains invalid characters"
    )
    @Size(max = 50)
    private String lastName;

    private Gender gender;

    @Size(max = 160, message = "Bio must be at most 160 characters")
    @Pattern(
            regexp = "^[^<>]*$",
            message = "Bio contains invalid characters"
    )
    private String bio;
}
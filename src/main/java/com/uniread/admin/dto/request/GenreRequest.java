package com.uniread.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GenreRequest {

    @NotBlank(message = "Name is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9 ]+$",
            message = "Invalid value for genre name"
    )
    @Size(max = 30, message = "Name must be at most 30 characters")
    private String name;

    @Pattern(
            regexp = "^[a-zA-Z0-9\\s.,!?]+$",
            message = "Invalid value for description"
    )
    @Size(max = 200, message = "Description must be at most 200 characters")
    private String description;
}

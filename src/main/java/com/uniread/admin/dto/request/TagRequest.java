package com.uniread.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TagRequest {


    @NotBlank(message = "Tag name is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9 ]+$",
            message = "Invalid value for tag name"
    )
    @Size(max = 15, message = "Name must be at most 15 characters")
    private String name;
}

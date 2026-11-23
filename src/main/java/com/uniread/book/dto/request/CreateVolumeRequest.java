package com.uniread.book.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateVolumeRequest {

    @NotNull(message = "Volume sequence is required")
    private Integer volumeNumber;

    @NotBlank(message = "Volume title is required")
    @Pattern(
            regexp = "^[^<>]*$",
            message = "Volume title contains invalid characters"
    )
    private String title;
    @Pattern(
            regexp = "^[^<>]*$",
            message = "Volume description contains invalid characters"
    )
    private String description;
}

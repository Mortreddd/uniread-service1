package com.uniread.book.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Setter
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class CreateBookRequest {

    @NotNull(message = "Book title is required")
    @Size(min = 2, max = 100, message = "Book title must be between 2 and 100 characters" )
    @Pattern( regexp = "^[a-zA-Z0-9À-ÿ .,'!?&:;()\\-]+$", message = "Book title contains invalid characters" )
    private String title;

    @NotNull(message = "Synopsis is required")
    @Size( min = 10, max = 2000, message = "Synopsis must be between 10 and 2000 characters" )
    @Pattern( regexp = "^[a-zA-Z0-9À-ÿ .,'!?&:;()\\-\\n]+$", message = "Synopsis contains invalid characters" )
    private String description;

    @NotNull(message = "Mature content is required")
    private Boolean matured;
    @NotNull(message = "Book cover is required")
    private MultipartFile cover;
    @NotNull(message = "Genre is required")
    private List<UUID> genres;

    private List<UUID> collaborators;
}

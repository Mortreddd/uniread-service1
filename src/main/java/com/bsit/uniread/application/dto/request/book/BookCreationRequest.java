package com.bsit.uniread.application.dto.request.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookCreationRequest {

    @NotNull(message = "Title is null")
    private String title;
    @NotNull(message = "Author of the book is required")
    private UUID authorId;
    @NotNull(message = "Cover photo of the book is required")
    private MultipartFile photo;
    @NotBlank(message = "Synopsis must not be blank")
    private String description;
    private List<Integer> genreIds;
    private Boolean matured;

}

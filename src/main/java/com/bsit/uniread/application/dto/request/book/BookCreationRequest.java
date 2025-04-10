package com.bsit.uniread.application.dto.request.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
public class BookCreationRequest {

    @NotNull(message = "Title is null")
    private String title;
    @NotNull(message = "Author of the book is required")
    private UUID authorId;
    @NotNull(message = "Cover photo of the book is required")
    private MultipartFile coverPhoto;
    @NotBlank(message = "Synosis must not be blank")
    private String description;

    private Integer[] genreIds;
    private Boolean matured;
}

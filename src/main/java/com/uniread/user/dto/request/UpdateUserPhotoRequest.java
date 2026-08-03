package com.uniread.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserPhotoRequest {
    @NotBlank
    private String secureUrl;
    @NotBlank
    private String publicId;
}

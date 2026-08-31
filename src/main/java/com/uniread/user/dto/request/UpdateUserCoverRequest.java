package com.uniread.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
public class UpdateUserCoverRequest {
    @NotNull(message = "Avatar is required")
    private MultipartFile cover;
}

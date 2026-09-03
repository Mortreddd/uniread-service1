package com.uniread.user.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfilePhotoResponse {
    private String publicId;
    private String photoUrl;
}

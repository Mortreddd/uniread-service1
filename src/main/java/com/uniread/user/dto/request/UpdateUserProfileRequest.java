package com.uniread.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserProfileRequest {
    private String username;
    private String firstName;
    private String lastName;
}

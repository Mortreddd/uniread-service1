package com.uniread.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class UserSearchDto {
    private UUID id;
    private String displayName;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String username;
    private Boolean isEmailVerified;
}

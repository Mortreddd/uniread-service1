package com.uniread.social.dto.response;

import com.uniread.user.domain.entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthUserFollowDto {
    private UUID userId;
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private Gender gender;
    private String photoUrl;
}

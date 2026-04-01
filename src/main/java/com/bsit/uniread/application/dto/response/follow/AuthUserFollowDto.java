package com.bsit.uniread.application.dto.response.follow;

import com.bsit.uniread.domain.entities.user.Gender;
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

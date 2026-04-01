package com.bsit.uniread.application.dto.response.user;

import com.bsit.uniread.domain.entities.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserProfileDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String displayName;
    private String bio;
    private String avatarPhoto;
    private String coverPhoto;
    private Gender gender;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

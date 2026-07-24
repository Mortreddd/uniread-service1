package com.uniread.user.dto.response;

import com.uniread.user.domain.entities.Gender;
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

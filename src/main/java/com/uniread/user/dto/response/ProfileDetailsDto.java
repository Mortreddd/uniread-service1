package com.uniread.user.dto.response;

import com.uniread.user.domain.entities.Gender;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProfileDetailsDto {

    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String displayName;
    private Gender gender;
    private String bio;


    private String avatarUrl;
    private String avatarPublicId;

    private String coverUrl;
    private String coverPublicId;




}

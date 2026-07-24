package com.uniread.user.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SimpleUserInfo {

    private UUID id;
    private String username;
    private String fullName;
    private String displayName;
    private String userPhoto;

}

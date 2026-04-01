package com.bsit.uniread.application.dto.response.user;

import com.bsit.uniread.domain.entities.user.User;
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

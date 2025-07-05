package com.bsit.uniread.application.dto.response.user;

import com.bsit.uniread.domain.entities.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserChatInfo {

    private UUID id;
    private String username;
    private String fullName;
    private String firstName;
    private String lastName;

    public UserChatInfo(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

}

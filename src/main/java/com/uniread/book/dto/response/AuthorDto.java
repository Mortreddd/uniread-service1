package com.uniread.book.dto.response;

import com.uniread.user.domain.entities.Gender;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class AuthorDto {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String avatarUrl;
}

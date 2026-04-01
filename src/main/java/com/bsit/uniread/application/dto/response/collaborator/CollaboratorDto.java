package com.bsit.uniread.application.dto.response.collaborator;

import com.bsit.uniread.application.dto.response.book.SimpleBookInfoDto;
import com.bsit.uniread.application.dto.response.user.SimpleUserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CollaboratorDto {

    private UUID id;
    private SimpleUserInfo user;
    private SimpleBookInfoDto book;
    private Instant bannedAt;
    private Instant unbannedAt;
    private Instant createdAt;
    private Instant updatedAt;


}

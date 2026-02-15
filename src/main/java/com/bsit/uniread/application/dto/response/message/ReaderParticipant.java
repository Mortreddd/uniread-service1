package com.bsit.uniread.application.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Setter
public class ReaderParticipant {

    private UUID participantId;
    private LocalDateTime lastReadAt;

}

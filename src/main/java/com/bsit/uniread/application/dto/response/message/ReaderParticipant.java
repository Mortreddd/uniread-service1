package com.bsit.uniread.application.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReaderParticipant {

    private UUID participantId;
    private Instant lastReadAt;

}

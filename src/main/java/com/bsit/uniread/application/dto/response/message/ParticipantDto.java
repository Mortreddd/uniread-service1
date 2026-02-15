package com.bsit.uniread.application.dto.response.message;

import com.bsit.uniread.application.dto.response.user.SimpleUserInfo;
import com.bsit.uniread.domain.entities.message.Participant;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ParticipantDto {
    private UUID id;
    private UUID conversationId;
    private SimpleUserInfo user;
    private Boolean muted;
    private Boolean archived;
    private LocalDateTime lastReadAt;
    private LocalDateTime addedAt;

    public ParticipantDto(Participant participant) {
        this.id = participant.getId();
        this.conversationId = participant.getConversation().getId();
        this.user = new SimpleUserInfo(participant.getUser());
        this.muted =  participant.getMuted();
        this.archived = participant.getArchived();
        this.lastReadAt = participant.getLastReadAt();
        this.addedAt = participant.getAddedAt();
    }

}

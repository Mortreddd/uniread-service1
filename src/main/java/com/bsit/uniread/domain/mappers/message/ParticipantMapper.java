package com.bsit.uniread.domain.mappers.message;

import com.bsit.uniread.application.dto.response.message.ParticipantDto;
import com.bsit.uniread.domain.entities.message.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "fullName", expression = "java(user.getProfile().getFirstName() + \" \" + user.getProfile().getLastName())")
    ParticipantDto toDto(Participant participant);
}

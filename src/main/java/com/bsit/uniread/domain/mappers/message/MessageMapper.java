package com.bsit.uniread.domain.mappers.message;


import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.domain.entities.message.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderName", expression = "java(sender.getSender().getProfile().getFirstName() + \" \" + sender.getSender().getProfile().getLastName())")
    MessageDto toMessageDto(Message message);

}
package com.uniread.chat.service;

import com.uniread.chat.dto.request.ConversationMessageFilter;
import com.uniread.chat.dto.request.NewMessageRequest;
import com.uniread.chat.dto.response.MessageDto;
import com.uniread.chat.domain.entities.Conversation;
import com.uniread.chat.domain.entities.Message;
import com.uniread.chat.domain.entities.MessageStatus;
import com.uniread.user.domain.entities.User;
import com.uniread.chat.mappers.MessageMapper;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.chat.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;


    public Page<MessageDto> getUserConversationMessages(UUID conversationId, ConversationMessageFilter filter, UUID currentUserId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "m.createdAt");
        Pageable pageable = PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort);
        return messageRepository.findConversationMessages(conversationId, currentUserId, pageable);
    }

    public MessageDto getMessageById(UUID messageId) {
        return messageRepository.findMessageById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Message %s is not found", messageId)));
    }

    @Transactional
    public MessageDto createNewMessage(NewMessageRequest request, UUID senderId) {
        var type = request.getMessageType();
        var content = request.getContent();
        var sender = User.builder().id(senderId).build();
        var convo = Conversation.builder().id(request.getConversationId()).build();

        var message = Message.builder()
                .sender(sender)
                .conversation(convo)
                .message(content)
                .messageType(type)
                .deliveredAt(Instant.now())
                .status(MessageStatus.SENT)
                .build();

        return messageMapper.toMessageDto(messageRepository.save(message));
    }

}

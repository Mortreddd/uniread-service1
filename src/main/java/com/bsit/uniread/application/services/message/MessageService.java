package com.bsit.uniread.application.services.message;

import com.bsit.uniread.application.dto.request.message.ConversationMessageFilter;
import com.bsit.uniread.application.dto.request.message.NewMessageRequest;
import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.application.services.conversation.ConversationService;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.domain.entities.message.MessageStatus;
import com.bsit.uniread.domain.entities.message.MessageType;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.mappers.message.MessageMapper;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.message.ConversationRepository;
import com.bsit.uniread.infrastructure.repositories.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;

//    @Transactional
//    public List<MessageDto> getUnreadMessages(UUID conversationId, UUID currentUserId) {
//        Conversation conversation = conversationRepository.findById(conversationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
//
//        conversationService.markConversationAsReadAndNotify(conversationId, currentUserId);
//        return messageRepository.findUnreadMessages(conversation.getId(), currentUser.getId());
//    }


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
    public MessageDto createNewMessage(NewMessageRequest request, UUID conversationId, UUID senderId) {
        var type = request.getMessageType();
        var content = request.getContent();
        var sender = User.builder().id(senderId).build();
        var convo = Conversation.builder().id(conversationId).build();

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

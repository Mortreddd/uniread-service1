package com.bsit.uniread.application.services.message;

import com.bsit.uniread.application.dto.request.message.MessageCreationRequest;
import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.handler.publishers.message.MessagePublisher;
import com.bsit.uniread.infrastructure.repositories.message.ConversationRepository;
import com.bsit.uniread.infrastructure.repositories.message.MessageRepository;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    private final MessagePublisher messagePublisher;

    /**
     * Get the messages from a selected conversation
     * @param conversationId
     * @return Page of Message
     */
    @Transactional(readOnly = true)
    public Page<Message> getMessagesByConversationId(UUID conversationId, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Conversation conversation = getConversationById(conversationId);

        return messageRepository.findByConversation(conversation, pageable);
    }

    /**
     * Get the conversation by id
     * @param conversationId
     * @return Conversation
     */
    @Transactional(readOnly = true)
    public Conversation getConversationById(UUID conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find conversation"));
    }

    /**
     * Create a new Message Object
     * @param messageCreationRequest
     */
    @Transactional
    public void createNewMessage(MessageCreationRequest messageCreationRequest) {
        Conversation conversation = getConversationById(messageCreationRequest.getConversationId());

        User sender = userService.getUserById(messageCreationRequest.getSenderId());
        
        MessageDto newMessage = new MessageDto(save(Message.builder()
                    .conversation(conversation)
                    .message(messageCreationRequest.getMessage())
                    .sender(sender)
                    .createdAt(DateUtil.now())
                    .build()
        ));

        messagePublisher.publishUserSentMessage(conversation, newMessage);
    }

    /**
     * Save the entity of Message
     * @param message
     * @return Message
     */
    public Message save(Message message) {
        return messageRepository.save(message);
    }


}

package com.bsit.uniread.application.services.message;

import com.bsit.uniread.application.dto.request.message.MessageCreationRequest;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.MessageRepository;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    /**
     * Accepts a userId as a parameter to retrieve all the receiver messages
     *
     * @params UUID userId
     * @return Page of Message List
     */

    public Page<Message> getReceiverMessages(UUID userId, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Message> messages = messageRepository.findAll(pageable);

        return messages;
    }

    /**
     * Create a new Message Object
     * @param messageCreationRequest
     * @return Message
     */

    public Message createMessage(MessageCreationRequest messageCreationRequest) {
        User receiver = userService.getUserById(messageCreationRequest.getReceiverId());
        User sender = userService.getUserById(messageCreationRequest.getSenderId());
        return Message.builder()
                .message(messageCreationRequest.getMessage())
                .receiver(receiver)
                .sender(sender)
                .createdAt(DateUtil.now())
                .readAt(null)
                .build();
    }
}

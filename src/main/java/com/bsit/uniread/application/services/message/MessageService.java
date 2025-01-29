package com.bsit.uniread.application.services.message;

import com.bsit.uniread.application.dto.request.message.MessageCreationRequest;
import com.bsit.uniread.application.dto.request.message.StartConversationRequest;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.handler.publishers.message.MessagePublisher;
import com.bsit.uniread.infrastructure.repositories.message.ConversationRepository;
import com.bsit.uniread.infrastructure.repositories.message.MessageRepository;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ParticipantService participantService;
    private final UserService userService;
    private final MessagePublisher messagePublisher;

    /**
     * Accepts a userId as a parameter to retrieve all the receiver messages
     *
     * @params UUID userId
     * @return Pagination of Message
     */
    public Page<Message> getMessagesByConversation(Conversation conversation, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return messageRepository.findByConversation(conversation, pageable);

    }

    /**
     * Get the messages from a conversation
     * @param conversationId
     * @return Page of Message
     */
    public Page<Message> getMessagesByConversationId(UUID conversationId, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Conversation conversation = getConversationById(conversationId);
        return getMessagesByConversation(conversation, pageNo, pageSize);
    }

    /**
     * Get the conversation by id
     * @param conversationId
     * @return Conversation
     */
    public Conversation getConversationById(UUID conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to locate conversation"));
    }


    /**
     * Get all the conversations of user based on involvement of user of every conversation
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return Page<Conversation>
     */
    public Page<Conversation> getConversationsByUserId(UUID userId, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        User user = userService.getUserById(userId);
        return conversationRepository.findByParticipants_User(user, pageable);
    }


    /**
     * Creates a new conversation
     * @param startConversationRequest
     * @return Conversation
     */
    public Conversation createConversation(StartConversationRequest startConversationRequest) {
        List<User> users = userService.getUsersById(List.of(startConversationRequest.getReceiverIds()));
        Conversation conversation = conversationRepository.save(Conversation.builder()
                .name(startConversationRequest.getConversationName())
                .isGroup(startConversationRequest.getIsGroup())
                .createdAt(DateUtil.now())
                .build()
        );
        List<Participant> participants = participantService.saveParticipants(conversation, users);
        conversation.setParticipants(participants);
        return conversationRepository.save(conversation);
    }

    /**
     * Create a new Message Object
     * @param messageCreationRequest
     * @return Message
     */
    public Message createMessage(MessageCreationRequest messageCreationRequest) {
        Conversation conversation = getConversationById(messageCreationRequest.getConversationId());
        User sender = userService.getUserById(messageCreationRequest.getSenderId());
        Message newMessage = messageRepository.save(
                Message.builder()
                    .conversation(conversation)
                    .message(messageCreationRequest.getMessage())
                    .sender(sender)
                    .createdAt(DateUtil.now())
                    .readAt(null)
                    .build()
        );

        messagePublisher.sendMessage(conversation, newMessage);

        return newMessage;
    }
}

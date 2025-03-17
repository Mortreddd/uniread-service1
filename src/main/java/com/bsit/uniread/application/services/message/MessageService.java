package com.bsit.uniread.application.services.message;

import com.bsit.uniread.application.dto.request.message.MessageCreationRequest;
import com.bsit.uniread.application.dto.request.message.StartConversationRequest;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
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

    /**
     * Get the messages from a selected conversation
     * @param conversationId
     * @return Page of Message
     */
    public Page<Message> getMessagesByConversationId(UUID conversationId, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Conversation conversation = getConversationById(conversationId);

        return messageRepository.findByConversation(conversation, pageable);
    }

    /**
     * Get the conversation by id
     * @param conversationId
     * @return Conversation
     */
    public Conversation getConversationById(UUID conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find conversation"));
    }

    /**
     * Get the conversation for the user associated with
     * @param userId
     * @return List of Conversation
     */
    public List<Conversation> getUserConversationsById(UUID userId) {

        List<Participant> userParticipant = participantService.getParticipantByUser(userService.getUserById(userId));

        return conversationRepository.findByParticipants(userParticipant);
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
    public Conversation createNewConversation(StartConversationRequest startConversationRequest) {
        List<User> users = userService.getUsersById(List.of(startConversationRequest.getReceiverIds()));
        Conversation conversation = createConversation(startConversationRequest);

        List<Participant> participants = participantService.createParticipants(conversation, users);
        conversation.setParticipants(participants);
        return conversationRepository.save(conversation);
    }

    /**
     * Create a new Message Object
     * @param messageCreationRequest
     * @return created message
     */
    public Message createNewMessage(MessageCreationRequest messageCreationRequest) {
        Conversation conversation = getConversationById(messageCreationRequest.getConversationId());

        User sender = userService.getUserById(messageCreationRequest.getSenderId());

        Message newMessage = Message.builder()
                    .conversation(conversation)
                    .message(messageCreationRequest.getMessage())
                    .sender(sender)
                    .createdAt(DateUtil.now())
                    .readAt(null)
                    .build();

        return createMessage(newMessage);
    }

    /**
     * Save the entity of Message
     * @param message
     * @return Message
     */
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    /**
     * Create a new conversation entity and save
     * @param request
     * @return created conversation
     */
    public Conversation createConversation(StartConversationRequest request) {
        return conversationRepository.save(Conversation.builder()
                .name(request.getConversationName())
                .isGroup(request.getIsGroup())
                .createdAt(DateUtil.now())
                .build()
        );
    }


}

package com.bsit.uniread.application.services.message;

import com.bsit.uniread.application.dto.request.message.MessageCreationRequest;
import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.handler.publishers.message.MessagePublisher;
import com.bsit.uniread.infrastructure.repositories.message.ConversationRepository;
import com.bsit.uniread.infrastructure.repositories.message.MessageRepository;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.infrastructure.repositories.message.ParticipantRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final ParticipantRepository participantRepository;
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    private final MessagePublisher messagePublisher;

    /**
     * Get the messages from a selected conversation
     * @param conversationId
     * @return Page of Message
     */
    @Transactional(readOnly = true)
    public Page<Message> getUserConversationMessages(UUID conversationId, int pageNo, int pageSize, String query) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        return messageRepository.findByConversationId(conversationId, pageable);
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
    public void createNewMessage(MessageCreationRequest messageCreationRequest, User sender) {
        Conversation conversation;

        if(messageCreationRequest.getConversationId() != null) {
            conversation = getConversationById(messageCreationRequest.getConversationId());
        } else {
            conversation = conversationRepository
                    .findOneOnOneConversation(sender.getId(), messageCreationRequest.getReceiverIds().getFirst())
                    .orElseGet(() -> newConversation(sender, messageCreationRequest.getReceiverIds(), messageCreationRequest.getIsGroup()));
        }

        // First save the message
        Message savedMessage = save(newMessage(conversation, messageCreationRequest.getMessage(), sender));
        MessageDto messageDto = new MessageDto(savedMessage);

        // Then update read status and publish event
        List<Participant> participants = conversation.getParticipants();
        messagePublisher.publishUserSentMessage(messageDto, participants);
    }

    /**
     * Save the entity of Message
     * @param message
     * @return Message
     */
    @Transactional
    public Message save(Message message) {
        return messageRepository.save(message);
    }


    private Message newMessage(Conversation conversation, String message, User sender) {
        return Message.builder()
                .conversation(conversation)
                .message(message)
                .sender(sender)
                .createdAt(DateUtil.now())
                .updatedAt(DateUtil.now())
                .build();
    }

    @Transactional
    private Conversation newConversation(User sender, List<UUID> receiverIds, boolean isGroup) {
        Conversation conversation = createConversation();
        conversation.setIsGroup(isGroup);
        conversation.setName(null);


        List<Participant> participants = userService.getUsersById(receiverIds)
                .stream()
                .map(user -> Participant.builder()
                        .user(user)
                        .conversation(conversation)
                        .readAt(null)
                        .build()
                ).collect(Collectors.toList());

        Participant senderParticipant = Participant.builder()
                .user(sender)
                .conversation(conversation)
                .readAt(DateUtil.now())
                .build();

        participants.add(senderParticipant);
        conversation.setParticipants(participants);
        return conversationRepository.save(conversation);
    }


    private Conversation createConversation() {
        return Conversation.builder()
                .name(null)
                .isGroup(false)
                .participants(new ArrayList<>())
                .build();
    }
}

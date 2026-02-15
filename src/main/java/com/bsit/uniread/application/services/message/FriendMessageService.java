package com.bsit.uniread.application.services.message;

import com.bsit.uniread.application.dto.request.message.FriendMessageRequest;
import com.bsit.uniread.application.dto.response.message.ConversationPreviewDto;
import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.application.services.participant.ParticipantService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.domain.entities.message.MessageType;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.message.ConversationRepository;
import com.bsit.uniread.infrastructure.repositories.message.MessageRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendMessageService {

    private final ParticipantService participantService;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;

    @Transactional
    public void newFriendMessage(FriendMessageRequest friendMessageRequest, Principal principal) {
        User currentUser = userService.getUserById(UUID.fromString(principal.getName()));
        User receiverUser = userService.getUserById(friendMessageRequest.getReceiverId());

        Conversation conversation = conversationRepository
                .findOneOnOneConversation(currentUser.getId(), receiverUser.getId())
                .orElseGet(() -> createNewOneOnOneConversation(currentUser, receiverUser));

        Message message = newMessage(friendMessageRequest.getMessage(), conversation, currentUser);


        participantService.updateLastReadAtByConversationAndUser(conversation, currentUser);
        conversation.setLastMessage(message);
        conversationRepository.save(conversation);

        broadcastMessage(conversation, message, currentUser, receiverUser);
    }

    private Conversation createNewOneOnOneConversation(User creator, User receiver) {
        Conversation conv = conversationRepository.save(Conversation.builder().isGroup(false).build());

        createParticipant(creator, conv);
        createParticipant(receiver, conv);

        return conv;
    }

    private void createParticipant(User user, Conversation conv) {
        participantService.save(Participant.builder()
                .conversation(conv)
                .user(user)
                .lastReadAt(DateUtil.now())
                .archived(false)
                .muted(false)
                .build());
    }

    private void broadcastMessage(Conversation conv, Message message, User sender, User receiver) {
        MessageDto dto = new MessageDto(message);

        ConversationPreviewDto conversationPreviewDto = conversationRepository.findByIdAndReceiverId(conv.getId(), receiver.getId());
        simpMessagingTemplate.convertAndSendToUser(receiver.getId().toString(), "/queue/chat-notifications", conversationPreviewDto);
        simpMessagingTemplate.convertAndSendToUser(sender.getId().toString(), "/queue/chat-notifications", conversationPreviewDto);

        simpMessagingTemplate.convertAndSendToUser(receiver.getId().toString(), "/queue/messages", dto);
        simpMessagingTemplate.convertAndSendToUser(sender.getId().toString(), "/queue/messages", dto);
    }

    private Message newMessage(String messageContent, Conversation conversation, User sender) {
        Message message = Message.builder()
                .message(messageContent)
                .messageType(MessageType.TEXT)
                .conversation(conversation)
                .sender(sender)
                .build();

        return messageRepository.save(message);
    }
}

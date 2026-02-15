package com.bsit.uniread.application.services.message;

import com.bsit.uniread.application.dto.request.message.ConversationMessageFilter;
import com.bsit.uniread.application.dto.request.message.GroupConversationMessageRequest;
import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Message;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
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

import java.security.Principal;
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

    @Transactional(readOnly = true)
    public List<Message> getUnreadMessages(UUID conversationId, Principal principal) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        User currentUser = userService.getUserById(UUID.fromString(principal.getName()));
        return messageRepository.findUnreadMessagesByConversationIdAndReceiverId(conversation.getId(), currentUser.getId());
    }


    /**
     * Get the messages from a selected conversation
     * @param conversationId
     * @param filter
     * @return Page of Message
     */
    @Transactional(readOnly = true)
    public Page<Message> getUserConversationMessages(UUID conversationId, ConversationMessageFilter filter) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort);
        return messageRepository.findByConversationId(conversationId, pageable);
    }
}

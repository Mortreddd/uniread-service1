package com.bsit.uniread.application.controllers.message;

import com.bsit.uniread.application.dto.request.message.FriendMessageRequest;
import com.bsit.uniread.application.dto.request.message.GroupConversationMessageRequest;
import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.application.dto.response.message.ReaderParticipant;
import com.bsit.uniread.application.services.conversation.ConversationService;
import com.bsit.uniread.application.services.message.FriendMessageService;
import com.bsit.uniread.application.services.message.MessageService;
import com.bsit.uniread.application.services.participant.ParticipantService;
import com.bsit.uniread.application.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping
@Slf4j
public class MessageController {

    private final ConversationService conversationService;
    private final ParticipantService participantService;
    private final MessageService messageService;
    private final FriendMessageService friendMessageService;
    private final UserService userService;

    @SubscribeMapping("/chat.{conversationId}")
    public List<MessageDto> sendNewConversationMessage(
            @DestinationVariable(value = "conversationId") UUID conversationId,
            Principal principal
    ) {
        UUID currentUserId = UUID.fromString(principal.getName());
        conversationService.markConversationAsReadAndNotify(conversationId, currentUserId);
        return messageService.getUnreadMessages(conversationId, principal)
                .stream().map(MessageDto::new).toList();
    }

    @MessageMapping("/chat/message")
    public void sendMessage(
            @Payload FriendMessageRequest friendMessageRequest,
            Principal principal
    ) {
        friendMessageService.newFriendMessage(friendMessageRequest, principal);
    }

    @MessageMapping("/chat/group")
    public void sendGroupMessage(
            @Payload GroupConversationMessageRequest groupConversationMessageRequest,
            Principal principal
    ) {

    }
}

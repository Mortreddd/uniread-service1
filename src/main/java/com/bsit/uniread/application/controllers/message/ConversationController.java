package com.bsit.uniread.application.controllers.message;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.message.ConversationFilter;
import com.bsit.uniread.application.dto.request.message.ConversationMessageFilter;
import com.bsit.uniread.application.dto.response.message.ConversationInfo;
import com.bsit.uniread.application.dto.response.message.ConversationPreviewDto;
import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.application.services.conversation.ConversationService;
import com.bsit.uniread.application.services.message.MessageService;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Api endpoint - /api/v1/conversations
 */
@RestController
@RequestMapping(path = ApiEndpoints.CONVERSATIONS)
@RequiredArgsConstructor
public class ConversationController {

    private final MessageService messageService;
    private final ConversationService conversationService;

    /**
     * Get the conversations of the user
     * @param filter
     * @return pagination of conversations of user
     */
    @GetMapping
    public ResponseEntity<Page<ConversationPreviewDto>> getUserConversations(
            @ModelAttribute ConversationFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<ConversationPreviewDto> conversations = conversationService
                .getUserConversationsById(userDetails.getId(), filter);

        return ResponseEntity.ok()
                .body(conversations);
    }

    @GetMapping(path = "/{conversationId}")
    public ResponseEntity<ConversationInfo> getUserConversationById(
            @PathVariable("conversationId") UUID conversationId
    ) {
        Conversation conversation = conversationService.getConversationById(conversationId);
        return ResponseEntity.ok()
                .body(new ConversationInfo(conversation.getId(), conversation.getName()));
    }
    /**
     * * Get all the messages on selected conversation
     * @param conversationId
     * @param filter
     * @return Pagination of messages with conversation
     */
    @GetMapping(path = "/{conversationId}/messages")
    public ResponseEntity<Page<MessageDto>> getConversationMessages(
            @PathVariable(name = "conversationId") UUID conversationId,
            @ModelAttribute ConversationMessageFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<MessageDto> messages = messageService
                .getUserConversationMessages(conversationId, filter)
                .map(MessageDto::new);

        return ResponseEntity.ok()
                .body(messages);
    }
}

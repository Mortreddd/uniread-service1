package com.uniread.chat.controllers;

import com.uniread.chat.dto.request.ConversationFilter;
import com.uniread.chat.dto.request.ConversationMessageFilter;
import com.uniread.chat.dto.request.ExistingConversationFilter;
import com.uniread.chat.dto.response.ConversationDetailDto;
import com.uniread.chat.dto.response.ConversationInfo;
import com.uniread.chat.dto.response.ConversationPreviewDto;
import com.uniread.chat.dto.response.MessageDto;
import com.uniread.chat.service.ConversationService;
import com.uniread.chat.service.MessageService;
import com.uniread.auth.domain.entities.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Api endpoint - /api/v1/conversations
 */
@RestController
@RequestMapping(path = "/conversations")
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

        if(userDetails == null) return null;


        Page<ConversationPreviewDto> conversations = conversationService
                .getUserConversationsById(userDetails.getId(), filter);

        return ResponseEntity.ok()
                .body(conversations);
    }

    @GetMapping(path = "/{conversationId}")
    public ResponseEntity<ConversationDetailDto> getUserConversationById(
            @PathVariable("conversationId") UUID conversationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok()
                .body(conversationService.getConversationById(conversationId, userDetails.getId()));
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
        if(userDetails == null) return null;

        Page<MessageDto> messages = messageService
                .getUserConversationMessages(conversationId, filter, userDetails.getId());

        return ResponseEntity.ok()
                .body(messages);
    }

    @GetMapping(path = "/recipient/{receiverId}")
    public ResponseEntity<ConversationInfo> getExistingConversationByRecipientId(
            @PathVariable("receiverId") UUID receiverId,
            @ModelAttribute  ExistingConversationFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) return null;

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(conversationService.getOneToOneConversation(receiverId, filter, userDetails.getId()));

    }
}

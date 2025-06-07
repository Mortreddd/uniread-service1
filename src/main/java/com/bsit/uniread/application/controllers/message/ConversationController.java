package com.bsit.uniread.application.controllers.message;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.message.ConversationDto;
import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.application.services.conversation.ConversationService;
import com.bsit.uniread.application.services.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Api endpoint - /api/v1/users/{userId}/conversations
 */
@RestController
@RequestMapping(path = ApiEndpoints.CONVERSATIONS)
@RequiredArgsConstructor
public class ConversationController {

    private final MessageService messageService;
    private final ConversationService conversationService;

    /**
     * Get the conversations of the user
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return pagination of conversations of user
     */
    @GetMapping
    public ResponseEntity<Page<ConversationDto>> getUserConversations(
            @PathVariable(name = "userId", required = false) UUID userId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        Page<ConversationDto> conversation = conversationService
                .getUserConversationsById(userId, pageNo, pageSize)
                .map(ConversationDto::new);

        return ResponseEntity.ok()
                .body(conversation);
    }

    @GetMapping(path = "/{conversationId}")
    public ResponseEntity<ConversationDto> getConversationById(
            @PathVariable(name = "userId") UUID userId,
            @PathVariable(name = "conversationId") UUID conversationId
    ) {
        ConversationDto conversation = new ConversationDto(conversationService.getConversationById(userId, conversationId));
        return ResponseEntity.ok()
                .body(conversation);
    }

    /**
     * Get all the messages on selected conversation
     * @param conversationId
     * @param pageNo
     * @param pageSize
     * @return Pagination of messages with conversation
     */
    @GetMapping(path = "/{conversationId}/messages")
    public ResponseEntity<Page<MessageDto>> getConversationMessages(
            @PathVariable(name = "userId") UUID userId,
            @PathVariable(name = "conversationId") UUID conversationId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        Page<MessageDto> messages = messageService
                .getMessagesByConversationId(conversationId, pageNo, pageSize)
                .map(MessageDto::new);

        return ResponseEntity.ok()
                .body(messages);
    }

}

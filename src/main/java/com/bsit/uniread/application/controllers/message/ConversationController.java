package com.bsit.uniread.application.controllers.message;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.services.message.MessageService;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    /**
     * Get the conversations of the user
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return pagination of conversations of user
     */
    @GetMapping
    public ResponseEntity<Page<Conversation>> getUserConversations(
            @PathVariable(name = "userId", required = false) UUID userId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok()
                .body(messageService.getConversationsByUserId(userId, pageNo, pageSize));
    }

    /**
     * Get all the messages on selected conversation
     * @param conversationId
     * @param pageNo
     * @param pageSize
     * @return Pagination of messages with conversation
     */
    @GetMapping(path = "/{conversationId}/messages")
    public ResponseEntity<Page<Message>> getConversationMessages(
            @PathVariable(name = "userId") UUID userId,
            @PathVariable(name = "conversationId") UUID conversationId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok()
                .body(messageService.getMessagesByConversationId(conversationId, pageNo, pageSize));
    }


//    /**
//     * Create
//     * @param startConversationRequest
//     * @return
//     */
//
//    @PostMapping(path = "/new")
//    public ResponseEntity<Conversation> createConversation(
//            @Valid @RequestBody StartConversationRequest startConversationRequest
//    ) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(messageService.createNewConversation(startConversationRequest));
//    }

}

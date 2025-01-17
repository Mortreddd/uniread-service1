package com.bsit.uniread.application.controllers.message;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.message.StartConversationRequest;
import com.bsit.uniread.application.services.message.MessageService;
import com.bsit.uniread.domain.entities.message.Conversation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = ApiEndpoints.CONVERSATIONS)
@RequiredArgsConstructor
public class ConversationController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<Page<Conversation>> getUserConversations(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "userId") UUID userId
    ) {

        return ResponseEntity.ok()
                .body(messageService.getConversationsByUserId(userId, pageNo, pageSize));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<Conversation> createConversation(
            @Valid @RequestBody StartConversationRequest startConversationRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.createConversation(startConversationRequest));
    }

}

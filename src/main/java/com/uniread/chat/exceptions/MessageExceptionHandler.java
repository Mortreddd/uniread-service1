package com.uniread.chat.exceptions;

import com.uniread.common.dto.api.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class MessageExceptionHandler {

    /**
     * Handles ConversationNotAllowedException - when a user tries to access a conversation they're not part of
     */
    @ExceptionHandler(ConversationNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleConversationNotAllowed(
            ConversationNotAllowedException exception,
            WebRequest request) {

        log.warn("Conversation not allowed: {}", exception.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Access Denied")
                .message(exception.getMessage())
                .path(getPath(request))
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
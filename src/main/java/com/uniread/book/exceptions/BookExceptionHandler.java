package com.uniread.book.exceptions;

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
public class BookExceptionHandler {

    /**
     * Handles AlreadyPublishedBookException - when trying to publish an already published book
     */
    @ExceptionHandler(AlreadyPublishedBookException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyPublishedBook(
            AlreadyPublishedBookException exception,
            WebRequest request) {

        log.warn("Book already published: {}", exception.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error("Already Published")
                .message(exception.getMessage())
                .path(getPath(request))
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(response);
    }

    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
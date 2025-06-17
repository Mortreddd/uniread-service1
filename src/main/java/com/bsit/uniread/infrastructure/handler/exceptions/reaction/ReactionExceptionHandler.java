package com.bsit.uniread.infrastructure.handler.exceptions.reaction;

import com.bsit.uniread.application.dto.api.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ReactionExceptionHandler {

    @ExceptionHandler(UnmatchedReactionException.class)
    public ResponseEntity<Object> resourceNotFoundException(UnmatchedReactionException exception, WebRequest request){
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .date(new Date())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(details);

    }


    @ExceptionHandler(AlreadyReactedException.class)
    public ResponseEntity<Object> resourceNotFoundException(AlreadyReactedException exception, WebRequest request){
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .date(new Date())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(details);

    }
}

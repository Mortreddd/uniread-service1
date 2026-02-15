package com.bsit.uniread.infrastructure.handler.exceptions.follow;


import com.bsit.uniread.application.dto.api.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Slf4j
@RestControllerAdvice
public class FollowExceptionHandler {

    @ExceptionHandler(AlreadyFollowingUserException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundException(AlreadyFollowingUserException exception, WebRequest request){
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .date(new Date())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();

        log.warn(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT.value())
                .body(details);
    }

    @ExceptionHandler(UserNotFollowedException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundException(UserNotFollowedException exception, WebRequest request){
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .date(new Date())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();

        log.warn(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(details);
    }


}

package com.bsit.uniread.infrastructure.handler.exceptions.auth;


import com.bsit.uniread.application.dto.api.ErrorResponse;
import com.bsit.uniread.domain.exceptions.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundException(InvalidCredentialsException exception, WebRequest request){
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .date(new Date())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(details);
    }

}

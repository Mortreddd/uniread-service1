package com.bsit.uniread.infrastructure.handler.exceptions.auth;


import com.bsit.uniread.application.dto.api.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> invalidCredentialsException(InvalidCredentialsException exception, WebRequest request){
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

    /**
     * Handles the validation exceptions
     * @param exception
     * @param request
     * @return Unprocessable Entity response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidProvidedValue(MethodArgumentNotValidException exception, WebRequest request) {
        String errorMessage = exception.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Invalid input.");

        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message(errorMessage)
                .date(new Date())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(details);

    }
}

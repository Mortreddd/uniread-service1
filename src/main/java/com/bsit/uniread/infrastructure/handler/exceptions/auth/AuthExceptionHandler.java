package com.bsit.uniread.infrastructure.handler.exceptions.auth;


import com.bsit.uniread.application.dto.api.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;

@Slf4j
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

        log.warn(exception.getMessage(), exception);
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

        log.warn(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(details);

    }

    /**
     * Handles the validation for authenticating user for wrong credentials
     * @param exception
     * @param request
     * @return Unprocessable Entity response
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> invalidProvidedValue(BadCredentialsException exception, WebRequest request) {
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message("Credentials do not match our record")
                .date(new Date())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(details);

    }

    /**
     * Handles the validation for authenticating user for wrong credentials
     * @param exception
     * @param request
     * @return Unprocessable Entity response
     */
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> invalidProvidedValue(TokenExpiredException exception, WebRequest request) {
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message("Session has been expired")
                .date(new Date())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(details);

    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> invalidTokenException(InvalidTokenException exception, WebRequest request) {
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(exception.getMessage())
                .date(new Date())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(details);

    }

}

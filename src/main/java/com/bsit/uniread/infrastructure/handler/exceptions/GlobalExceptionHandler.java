package com.bsit.uniread.infrastructure.handler.exceptions;

import com.bsit.uniread.application.dto.api.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception handler for field validations.
     * example: request validations
     * @param exception
     * @param request
     * @return responseEntity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolation(ConstraintViolationException exception, WebRequest request){
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .date(new Date())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(details);

    }

    /**
     * Specific resource is not found
     * example: searching for specific resource
     * @param exception
     * @param request
     * @return responseEntity
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException exception, WebRequest request){
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .date(new Date())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(details);

    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> duplicateResourceException(DuplicateResourceException exception, WebRequest request){
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .date(new Date())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(details);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentException(IllegalArgumentException exception, WebRequest request){
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .date(new Date())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(details);

    }

}

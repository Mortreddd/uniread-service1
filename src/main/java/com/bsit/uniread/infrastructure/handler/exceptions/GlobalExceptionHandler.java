package com.bsit.uniread.infrastructure.handler.exceptions;

import com.bsit.uniread.application.dto.api.ErrorResponse;
import com.bsit.uniread.domain.exceptions.BookNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(BookNotFoundException exception, WebRequest request){
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .date(new Date())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(details);

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> constrainedExceptions(
            ConstraintViolationException exception,
            WebRequest request
    ) {
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

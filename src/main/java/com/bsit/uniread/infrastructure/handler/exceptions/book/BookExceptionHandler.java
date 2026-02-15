package com.bsit.uniread.infrastructure.handler.exceptions.book;

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
public class BookExceptionHandler {

    @ExceptionHandler(AlreadyPublishedBookException.class)
    public ResponseEntity<Object> resourceNotFoundException(AlreadyPublishedBookException exception, WebRequest request){
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .date(new Date())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();

        log.warn(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(details);

    }
}

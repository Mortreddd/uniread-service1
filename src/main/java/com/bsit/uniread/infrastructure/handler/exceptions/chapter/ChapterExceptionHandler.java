package com.bsit.uniread.infrastructure.handler.exceptions.chapter;

import com.bsit.uniread.application.dto.api.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ChapterExceptionHandler {

    @ExceptionHandler(InvalidOwnerException.class)
    public ResponseEntity<Object> invalidOwnerException(
            InvalidOwnerException exception,
            WebRequest request
    ) {
        ErrorResponse details = ErrorResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .date(new Date())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(details);

    }
}

package com.uniread.social.exceptions;

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
public class FollowExceptionHandler {

    /**
     * Handles AlreadyFollowingUserException - when a user tries to follow someone they already follow
     */
    @ExceptionHandler(AlreadyFollowingUserException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyFollowing(
            AlreadyFollowingUserException exception,
            WebRequest request) {

        log.warn("Already following user: {}", exception.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Already Following")
                .message(exception.getMessage())
                .path(getPath(request))
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    /**
     * Handles UserNotFollowedException - when a user tries to unfollow someone they don't follow
     */
    @ExceptionHandler(UserNotFollowedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFollowed(
            UserNotFollowedException exception,
            WebRequest request) {

        log.warn("User not followed: {}", exception.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error("Not Following")
                .message(exception.getMessage())
                .path(getPath(request))
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(response);
    }

    /**
     * Handles self-follow attempts
     */
    @ExceptionHandler(CannotFollowSelfException.class)
    public ResponseEntity<ErrorResponse> handleCannotFollowSelf(
            CannotFollowSelfException exception,
            WebRequest request) {

        log.warn("Self-follow attempt: {}", exception.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Operation")
                .message(exception.getMessage())
                .path(getPath(request))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Helper method to extract path from WebRequest
     */
    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
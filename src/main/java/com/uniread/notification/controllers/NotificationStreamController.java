package com.uniread.notification.controllers;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.notification.service.NotificationStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/notifications")
public class
NotificationStreamController {

    private final NotificationStreamService streamService;

    @GetMapping(value = "/stream", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter getNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        return streamService.subscribe(userDetails);
    }

}

package com.uniread.chat.controllers;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.chat.dto.response.TotalUnreadMessage;
import com.uniread.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/messages")
@RequiredArgsConstructor
@RestController
public class MessageController {


    private final MessageService messageService;

    @GetMapping(path = "/unread-count")
    public ResponseEntity<TotalUnreadMessage> getTotalUnreadMessages(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var payload = messageService.getTotalUnreadMessages(userDetails);

        return ResponseEntity.ok(payload);
    }
}

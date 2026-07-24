package com.uniread.notification.controllers;

import com.uniread.notification.dto.request.NotificationFilter;
import com.uniread.notification.dto.response.NotificationDto;
import com.uniread.notification.service.NotificationService;
import com.uniread.auth.domain.entities.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Api Endpoint - /api/v1/notifications
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<NotificationDto>> getUserNotification(
            @ModelAttribute NotificationFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    )  {

        return ResponseEntity.ok().body(null);
    }
}

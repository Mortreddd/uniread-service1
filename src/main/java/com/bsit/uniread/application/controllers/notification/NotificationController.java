package com.bsit.uniread.application.controllers.notification;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.notification.NotificationFilter;
import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.application.services.notification.NotificationService;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
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
@RequestMapping(path = ApiEndpoints.NOTIFICATIONS)
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

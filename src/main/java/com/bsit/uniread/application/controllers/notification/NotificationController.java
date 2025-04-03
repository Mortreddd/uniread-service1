package com.bsit.uniread.application.controllers.notification;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.application.services.NotificationService;
import com.bsit.uniread.domain.entities.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Api Endpoint - /api/v1/users/{userId}/notifications
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = ApiEndpoints.USER_NOTIFICATION)
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Get the user's notification
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param query
     * @return page of notification
     */
    @GetMapping
    public ResponseEntity<Page<NotificationDto>> getUserNotification(
            @PathVariable(name = "userId") UUID userId,
            @RequestParam(name = "pageNo", required = false) int pageNo,
            @RequestParam(name = "pageSize", required = false) int pageSize,
            @RequestParam(name = "query", required = false) String query
    )  {
        Page<NotificationDto> notifications = notificationService.getUserNotifications(userId, pageNo, pageSize, query)
                .map(NotificationDto::new);

        return ResponseEntity.ok().body(notifications);
    }
}

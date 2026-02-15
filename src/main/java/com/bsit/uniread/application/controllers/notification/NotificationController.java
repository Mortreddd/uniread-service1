package com.bsit.uniread.application.controllers.notification;

import com.bsit.uniread.application.constants.ApiEndpoints;
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

    /**
     * Get the user's notification
     * @param pageNo
     * @param pageSize
     * @param query
     * @return page of notification
     */
    @GetMapping
    public ResponseEntity<Page<NotificationDto>> getUserNotification(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "query", defaultValue = "", required = false) String query,
            @RequestParam(name = "sortBy", defaultValue = "asc", required = false) String sortBy,
            @RequestParam(name = "orderBy", defaultValue = "createdAt", required = false) String orderBy,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    )  {
        Page<NotificationDto> notifications = notificationService.getCurrentUserNotifications(customUserDetails, pageNo, pageSize, query, sortBy, orderBy, startDate, endDate)
                .map(NotificationDto::new);

        return ResponseEntity.ok().body(notifications);
    }
}

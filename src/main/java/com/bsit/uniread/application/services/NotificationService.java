package com.bsit.uniread.application.services;

import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.Notification;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.NotificationRepository;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserService userService;
    private final NotificationRepository notificationRepository;

    /**
     * Get the user's notification
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param query
     * @return page of notification
     */
    public Page<Notification> getUserNotifications(UUID userId, int pageNo, int pageSize, String query) {
        User user = userService.getUserById(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if(StringUtil.isNullOrEmpty(query)) {
            return notificationRepository.findByUser(user, pageable);
        }

        return notificationRepository.findByUserAndTitleContainingIgnoreCase(user, query, pageable);
    }

    /**
     * Create a new notification
     * @param receiver
     * @param title
     * @param description
     * @return notification
     */
    public Notification createNotification(User receiver, String title, String description) {
        return notificationRepository.save(
                Notification.builder()
                        .title(title)
                        .description(description)
                        .user(receiver)
                        .isRead(false)
                        .build()
        );
    }

    public void notifyUsers(String title, String description, List<User> users) {
        List<Notification> notifications = users.stream()
                .map(user ->
                        Notification.builder()
                            .title(title)
                            .description(description)
                            .user(user)
                            .build()
                        )
                .toList();

        notificationRepository.saveAll(notifications);
    }
}

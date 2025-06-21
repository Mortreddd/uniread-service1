package com.bsit.uniread.application.services;

import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.Notification;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.NotificationRepository;
import com.bsit.uniread.infrastructure.specifications.notification.NotificationSpecification;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserService userService;
    private final NotificationRepository notificationRepository;

    /**
     * Get the authenticated user's notification
     * @param user
     * @param pageNo
     * @param pageSize
     * @param query
     * @return page of notification
     */
    @Transactional(readOnly = true)
    public Page<Notification> getCurrentUserNotifications(CustomUserDetails user, int pageNo, int pageSize, String query, String sortBy, String orderBy, String startDate, String endDate) {
        Sort.Direction direction = sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, orderBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<Notification> notificationSpecification = Specification.where(
                NotificationSpecification.hasQuery(query)
        );
        
        return notificationRepository.findAll(notificationSpecification, pageable);
    }

    /**
     * Create a new notification
     * @param receiver
     * @param title
     * @param description
     * @return notification
     */
    @Transactional(readOnly = true)
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

        saveNotifications(notifications);
    }

    @Transactional
    public void saveNotifications(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }
}

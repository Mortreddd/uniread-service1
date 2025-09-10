package com.bsit.uniread.application.services.notification;

import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.notification.Notification;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.NotificationRepository;
import com.bsit.uniread.infrastructure.specifications.notification.NotificationSpecification;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Notification> newNotifications(List<User> users, String title, String description) {
        return users.stream()
                .map(u -> newNotification(u, title, description))
                .toList();
    }

    private Notification newNotification(User user, String title, String description) {
        return Notification.builder()
                .isRead(false)
                .user(user)
                .title(title)
                .description(description)
                .createdAt(DateUtil.now())
                .build();
    }

    @Transactional
    public List<Notification> saveNotifications(List<Notification> notifications) {
        return notificationRepository.saveAll(notifications);
    }
}

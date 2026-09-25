package com.uniread.notification.repositories;

import com.uniread.notification.domain.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID>,
        JpaSpecificationExecutor<Notification> {

    // Get the user's notification
    Page<Notification> findByUserId(UUID userId, Pageable pageable);

    @Query("SELECT COUNT(n) > 0 FROM Notification n WHERE n.id = :notificationId AND n.user.id = :userId")
    boolean isOwner(@Param("notificationId") UUID notificationId, @Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE Notification SET isClicked = true, updatedAt = CURRENT_TIMESTAMP WHERE id = :notificationId")
    void updatedClickedNotification(@Param("notificationId") UUID notificationId);

    @Modifying
    @Query("UPDATE Notification SET isRead = true, updatedAt = CURRENT_TIMESTAMP WHERE user.id = :userId")
    void markReadNotifications(@Param("userId") UUID userId);

    @Query(value = """
        SELECT COALESCE(COUNT(n.id), 0)
        FROM notifications n
        WHERE n.user_id = :userId AND n.is_read = FALSE
    """, nativeQuery = true)
    long countUnread(@Param("userId") UUID userId);
}

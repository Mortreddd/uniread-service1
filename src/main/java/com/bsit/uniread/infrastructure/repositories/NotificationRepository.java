package com.bsit.uniread.infrastructure.repositories;

import com.bsit.uniread.domain.entities.Notification;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID>,
        CrudRepository<Notification, UUID> {

    // Get the user's notification
    Page<Notification> findByUser(User user, Pageable pageable);

    // Get the user's notification with search parameter
    Page<Notification> findByUserAndTitleContainingIgnoreCase(User user, String query, Pageable pageable);
}

package com.uniread.notification.repositories;

import com.uniread.notification.domain.entities.Notification;
import com.uniread.user.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID>,
        CrudRepository<Notification, UUID>, JpaSpecificationExecutor<Notification> {

    // Get the user's notification
    Page<Notification> findByUser(User user, Pageable pageable);

}

package com.bsit.uniread.infrastructure.repositories;

import com.bsit.uniread.domain.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID>,
        CrudRepository<Notification, UUID> {
}

package com.bsit.uniread.infrastructure.repositories;

import com.bsit.uniread.domain.entities.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID>,
        CrudRepository<Message, UUID> {

    Page<Message> findReceiverMessagesById(UUID userId, Pageable pageable);
}

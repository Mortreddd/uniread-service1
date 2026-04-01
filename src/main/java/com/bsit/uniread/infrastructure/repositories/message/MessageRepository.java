package com.bsit.uniread.infrastructure.repositories.message;

import com.bsit.uniread.application.dto.response.message.MessageDto;
import com.bsit.uniread.domain.entities.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID>,
        CrudRepository<Message, UUID> {

    @Query("""
    SELECT new com.bsit.uniread.application.dto.response.message.MessageDto(
        m.id,
        m.conversation.id,
        sender.id,
        CONCAT(sender.firstName, ' ', sender.lastName),
        m.message,
        m.deliveredAt,
        m.createdAt
    )
    FROM Message m
    JOIN m.sender sender
    JOIN m.conversation c
    JOIN c.participants p
    WHERE c.id = :conversationId
      AND p.user.id = :receiverId
      AND m.createdAt > p.lastReadAt
    ORDER BY m.createdAt DESC
    LIMIT 15
    """)
    List<MessageDto> findUnreadMessages(
            @Param("conversationId") UUID conversationId,
            @Param("receiverId") UUID receiverId
    );

    @Query("""
    SELECT new com.bsit.uniread.application.dto.response.message.MessageDto(
        m.id,
        m.conversation.id,
        sender.id,
        CONCAT(profile.firstName, ' ', profile.lastName),
        m.message,
        m.deliveredAt,
        m.createdAt
    )
    FROM Message m
    JOIN m.sender sender
    JOIN sender.profile
    WHERE m.id = :messageId
    """)
    Optional<MessageDto> findMessageById(@Param("messageId") UUID messageId);

    @Query(
        value = """
        SELECT new com.bsit.uniread.application.dto.response.message.MessageDto(
            m.id,
            m.conversation.id,
            sender.id,
            CONCAT(sender.firstName, ' ', sender.lastName),
            m.message,
            m.deliveredAt,
            m.createdAt
        )
        FROM Message m
        JOIN m.sender sender
        WHERE m.conversation.id = :conversationId
        """,
        countQuery = """
        SELECT COUNT(m) FROM Message m WHERE m.conversation.id = :conversationId
        """
    )
    Page<MessageDto> findConversationMessages(
            @Param("conversationId") UUID conversationId,
            @Param("currentUserId") UUID currentUserId,
            Pageable pageable
    );
}

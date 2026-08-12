package com.uniread.chat.repositories;

import com.uniread.chat.dto.response.MessageDto;
import com.uniread.chat.domain.entities.Message;
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

    @Query(
        value = """
        SELECT new com.uniread.chat.dto.response.MessageDto(
            m.id,
            m.conversation.id,
            m.sender.id,
            m.senderName,
            m.senderPhoto,
            m.messageType,
            m.message,
            m.deliveredAt,
            m.createdAt
        )
        FROM Message m
        JOIN m.conversation.participants p
        WHERE m.conversation.id = :conversationId
        AND p.user.id = :userId
        AND (p.deletedAt IS NULL OR m.createdAt > p.deletedAt)
        """,
        countQuery = """
        SELECT COUNT(m)
        FROM Message m
        JOIN Participant p
          ON m.conversation.id = p.conversation.id
          AND p.user.id = :userId
        WHERE m.conversation.id = :conversationId
        AND (p.deletedAt IS NULL OR m.createdAt > p.deletedAt)
        """
    )
    Page<MessageDto> findConversationMessages(
            @Param("conversationId") UUID conversationId,
            Pageable pageable,
            @Param("userId") UUID userId
    );
}

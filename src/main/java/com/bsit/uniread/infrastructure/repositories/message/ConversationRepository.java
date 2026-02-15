package com.bsit.uniread.infrastructure.repositories.message;

import com.bsit.uniread.application.dto.response.message.ConversationPreviewDto;
import com.bsit.uniread.domain.entities.message.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID>, CrudRepository<Conversation, UUID> {

    @Query(
    value = """
        SELECT new com.bsit.uniread.application.dto.response.message.ConversationPreviewDto(
            c.id,
            CASE
                WHEN c.isGroup = false AND COUNT(DISTINCT friend.user.id) <= 1
                THEN CONCAT(friend.user.firstName, ' ', friend.user.lastName)
                ELSE c.name
            END,
            c.avatar,
            COUNT(DISTINCT u.id),
            COUNT(DISTINCT u.id) > 0,
            self.muted,
            self.archived,
            c.isGroup,
            CASE
                WHEN lm.sender.id = :currentUserId THEN CONCAT('You:', ' ', lm.message)
                ELSE CONCAT(lm.sender.firstName, ' ', lm.sender.lastName, ': ', lm.message)
            END,
            lm.createdAt
        )
        FROM Conversation c
        JOIN Participant self ON self.user.id = :currentUserId AND c.id = self.conversation.id AND self.archived = :archived
        LEFT JOIN Participant friend ON friend.user.id <> :currentUserId AND c.id = friend.conversation.id
        LEFT JOIN c.lastMessage lm
        LEFT JOIN Message u ON u.createdAt > self.lastReadAt AND u.sender.id <> :currentUserId AND c.id = u.conversation.id
        GROUP BY
            c.id,
            c.name,
            c.avatar,
            self.muted,
            self.archived,
            c.isGroup,
            lm.message,
            lm.createdAt,
            friend.user.firstName,
            friend.user.lastName
        """,
    countQuery = """
        SELECT COUNT(DISTINCT c.id)
        FROM Conversation c
        JOIN c.participants p
        WHERE p.user.id = :currentUserId
          AND p.archived = :archived
        """
    )
    Page<ConversationPreviewDto> findConversationsByParticipantId(
            @Param("currentUserId") UUID userId,
            @Param("archived") boolean isArchived,
            Pageable pageable
    );


    @Query("""
        SELECT new com.bsit.uniread.application.dto.response.message.ConversationPreviewDto(
            c.id,
            CASE
                WHEN c.isGroup = false
                THEN CONCAT(friend.user.firstName, ' ', friend.user.lastName)
                ELSE c.name
            END,
            c.avatar,
            COUNT(DISTINCT u.id),
            COUNT(DISTINCT u.id) > 0,
            self.muted,
            self.archived,
            c.isGroup,
            CASE
                WHEN lm.sender.id = :currentUserId THEN CONCAT('You:', ' ', lm.message)
                ELSE CONCAT(lm.sender.firstName, ' ', lm.sender.lastName, ': ', lm.message)
            END,
            lm.createdAt
        )
        FROM Conversation c
    
        JOIN c.participants self
            ON self.user.id = :receiverId
    
        JOIN c.participants friend
            ON friend.user.id <> :receiverId
    
        LEFT JOIN c.lastMessage lm
    
        LEFT JOIN c.messages u
            ON u.createdAt > self.lastReadAt
    
        WHERE c.id = :id
          AND c.isGroup = false
    
        GROUP BY
            c.id,
            c.name,
            c.avatar,
            self.muted,
            self.archived,
            c.isGroup,
            lm.message,
            lm.createdAt,
            friend.user.firstName,
            friend.user.lastName
    """)
    ConversationPreviewDto findByIdAndReceiverId(
            @Param("id") UUID id,
            @Param("receiverId") UUID receiverId
    );


    @Query(value = """
            SELECT c FROM Conversation AS c
            JOIN c.participants AS p1
            JOIN c.participants AS p2
            WHERE p1.user.id = :userId1
              AND p2.user.id = :userId2
              AND c.isGroup = false
            """
    )
    Optional<Conversation> findOneOnOneConversation(@Param("userId1") UUID senderId, @Param("userId2") UUID receiverId);

    @Modifying
    @Query("""
        UPDATE Participant p
        SET p.lastReadAt = CURRENT_TIMESTAMP
        WHERE p.user.id = :readerId AND p.conversation.id = :conversationId
    """)
    void updateConversationIdAndReaderId(
            @Param("conversationId") UUID conversationId,
            @Param("readerId") UUID readerId
    );

}

package com.bsit.uniread.infrastructure.repositories.message;

import com.bsit.uniread.application.dto.response.message.ConversationDetailDto;
import com.bsit.uniread.application.dto.response.message.ConversationPreviewDto;
import com.bsit.uniread.domain.entities.message.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID>, CrudRepository<Conversation, UUID> {

    @Query(value = """
    SELECT new com.bsit.uniread.application.dto.response.message.ConversationPreviewDto(
        c.id,
        CASE 
            WHEN c.isGroup = false 
            THEN (SELECT CONCAT(p.user.firstName, ' ', p.user.lastName) 
                  FROM Participant p 
                  WHERE p.conversation.id = c.id AND p.user.id <> :currentUserId)
            ELSE c.name 
        END,
        c.avatar,
        COUNT(DISTINCT u.id),
        COUNT(DISTINCT u.id) > 0,
        self.muted,
        self.archived,
        c.isGroup,
        lm.createdAt
    )
    FROM Conversation c
    JOIN c.participants self 
    LEFT JOIN c.lastMessage lm
    LEFT JOIN c.messages u ON u.createdAt > self.lastReadAt 
                          AND u.sender.id <> :currentUserId
    WHERE self.user.id = :currentUserId 
      AND self.archived = :archived
    GROUP BY 
        c.id, c.name, c.avatar, self.muted, self.archived, c.isGroup, lm.createdAt
    """,
    countQuery = """
    SELECT COUNT(c.id)
    FROM Conversation c
    JOIN c.participants p
    WHERE p.user.id = :currentUserId AND p.archived = :archived
    """
    )
    Page<ConversationPreviewDto> findConversationsByParticipantId(
        @Param("currentUserId") UUID userId,
        @Param("archived") boolean isArchived,
        Pageable pageable
    );



    @EntityGraph(attributePaths = {"participants", "lastMessage.sender.profile"})
    Optional<Conversation> findById(UUID id);


    @Query(value = """
            SELECT c FROM Conversation AS c
            JOIN c.participants AS p1
            JOIN c.participants AS p2
            WHERE p1.user.id = :userId1
              AND p2.user.id = :userId2
              AND c.isGroup = false
            """
    )
    Optional<Conversation> findOneOnOneConversation(@Param("userId1") UUID senderId, @Param("userId2") UUID receiverId, @Param("isGroup") Boolean isGroup);

    @Query("""
        SELECT new com.bsit.uniread.application.dto.response.message.ConversationDetailDto(
            c.id,
            CASE 
                WHEN c.isGroup = false 
                THEN CONCAT(friend.user.firstName, ' ', friend.user.lastName) 
                ELSE c.name 
            END,
            c.avatar,
            self.muted,
            self.archived,
            c.isGroup,
            friend.user.id
        )
        FROM Conversation c
        JOIN c.participants self
        JOIN c.participants friend
        WHERE c.id = :id
          AND self.user.id = :receiverId
          AND friend.user.id <> :receiverId
          AND c.isGroup = false
    """)
    Optional<ConversationDetailDto> findUserConversationById(
            @Param("id") UUID id,
            @Param("receiverId") UUID receiverId
    );
    
}

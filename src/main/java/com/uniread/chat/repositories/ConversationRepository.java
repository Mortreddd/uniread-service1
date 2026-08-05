package com.uniread.chat.repositories;

import com.uniread.chat.dto.response.ConversationDetailDto;
import com.uniread.chat.dto.response.ConversationPreviewDto;
import com.uniread.chat.domain.entities.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @Query(value = """
    SELECT new com.uniread.chat.dto.response.ConversationPreviewDto(
        c.id,
        CASE
            WHEN c.isGroup = false
            THEN COALESCE((
                SELECT CONCAT(p.user.profile.firstName, ' ', p.user.profile.lastName)
                FROM Participant p
                WHERE p.conversation.id = c.id
                  AND p.user.id <> :currentUserId
            ), 'Unknown')
            ELSE c.name
        END,
        CASE
            WHEN c.isGroup = false THEN MAX(selfProfile.avatarUrl)
            ELSE c.avatarPhoto
        END,
        COUNT(DISTINCT u.id),
        CASE WHEN COUNT(DISTINCT u.id) > 0 THEN true ELSE false END,
        self.muted,
        self.archived,
        c.isGroup,
        new com.uniread.chat.dto.response.MessageDto(
            MAX(lm.id),
            c.id,
            MAX(lmSender.id),
            MAX(lmSenderProfile.displayName),
            MAX(lm.messageType),
            MAX(lm.message),
            MAX(lm.deliveredAt),
            MAX(lm.createdAt)
        )
    )
    FROM Conversation c
    JOIN c.participants self
    JOIN self.user selfUser
    JOIN selfUser.profile selfProfile
    LEFT JOIN c.lastMessage lm
    LEFT JOIN lm.sender lmSender
    LEFT JOIN lmSender.profile lmSenderProfile
    LEFT JOIN c.messages u
        ON (self.lastReadAt IS NULL OR u.createdAt > self.lastReadAt)
       AND u.sender.id <> :currentUserId
    WHERE self.user.id = :currentUserId AND lm.id IS NOT NULL
    GROUP BY
        c.id, c.name, c.avatarPhoto, self.muted, self.archived, c.isGroup, c.updatedAt
    """,
    countQuery = """
    SELECT COUNT(c.id)
    FROM Conversation c
    JOIN c.participants p
    WHERE p.user.id = :currentUserId
    """
    )
    Page<ConversationPreviewDto> findConversationsByParticipantId(
        @Param("currentUserId") UUID userId,
        Pageable pageable
    );

    @Query("""
        SELECT new com.uniread.chat.dto.response.ConversationPreviewDto(
            c.id,
        
            CASE
                WHEN c.isGroup = false THEN (
                    SELECT p2.user.profile.displayName
                    FROM Participant p2
                    WHERE p2.conversation.id = c.id
                      AND p2.user.id <> :currentUserId
                )
                ELSE c.name
            END,
        
            CASE
                WHEN c.isGroup = false THEN (
                    SELECT p2.user.profile.avatarUrl
                    FROM Participant p2
                    WHERE p2.conversation.id = c.id
                      AND p2.user.id <> :currentUserId
                )
                ELSE c.avatarPhoto
            END,
        
            COUNT(DISTINCT unread.id),
        
            CASE WHEN COUNT(DISTINCT unread.id) > 0 THEN true ELSE false END,
        
            self.muted,
            self.archived,
            c.isGroup,
        
            new com.uniread.chat.dto.response.MessageDto(
                lm.id,
                c.id,
                lmSender.id,
                lmSenderProfile.displayName,
                lm.messageType,
                lm.message,
                lm.deliveredAt,
                lm.createdAt
            )
        )
        FROM Conversation c
        
        JOIN c.participants self
        JOIN self.user selfUser
        JOIN selfUser.profile selfProfile
        
        LEFT JOIN c.participants other
            ON other.user.id <> :currentUserId
        LEFT JOIN other.user otherUser
        LEFT JOIN otherUser.profile otherProfile
        
        LEFT JOIN c.lastMessage lm
        LEFT JOIN lm.sender lmSender
        LEFT JOIN lmSender.profile lmSenderProfile
        
        LEFT JOIN c.messages unread
            ON (self.lastReadAt IS NULL OR unread.createdAt > self.lastReadAt)
            AND unread.sender.id <> :currentUserId
        
        WHERE self.user.id = :currentUserId
          AND c.id = :id
          AND lm.id IS NOT NULL
        
        GROUP BY
            c.id,
            c.name,
            c.avatarPhoto,
            c.isGroup,
            self.muted,
            self.archived,
            otherProfile.displayName,
            otherProfile.avatarUrl,
            lm.id,
            lmSender.id,
            lmSenderProfile.displayName,
            lm.messageType,
            lm.message,
            lm.deliveredAt,
            lm.createdAt
        """)
    Optional<ConversationPreviewDto> findConvoDetailById(
            @Param("id") UUID id,
            @Param("currentUserId") UUID authUserId
    );

    @Query(value = """
    SELECT c.*
    FROM conversations c
    WHERE c.is_group = false
    AND EXISTS (
        SELECT 1 FROM participants p1 
        WHERE p1.conversation_id = c.id 
        AND p1.user_id = :currentUserId
    )
    AND EXISTS (
        SELECT 1 FROM participants p2 
        WHERE p2.conversation_id = c.id 
        AND p2.user_id = :receiverId
    )
    LIMIT 1
    """, nativeQuery = true)
    Optional<Conversation> findDirectConversation(
            @Param("currentUserId") UUID currentUserId,
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
    Optional<Conversation> findOneOnOneConversation(@Param("userId1") UUID senderId, @Param("userId2") UUID receiverId, @Param("isGroup") Boolean isGroup);

    @Query("""
    SELECT new com.uniread.chat.dto.response.ConversationDetailDto(
        c.id,
        CASE 
            WHEN c.isGroup = false 
            THEN CONCAT(friend.user.profile.firstName, ' ', friend.user.profile.lastName) 
            ELSE c.name 
        END,
        c.avatarPhoto,
        self.muted,
        self.archived,
        c.isGroup,
        new com.uniread.chat.dto.response.MessageDto(
            lastMessage.id,
            friend.user.id,
            c.id,
            CASE 
                WHEN c.isGroup = false 
                THEN CONCAT(friend.user.profile.firstName, ' ', friend.user.profile.lastName) 
                ELSE c.name 
            END,
            lastMessage.messageType,
            lastMessage.message,
            lastMessage.deliveredAt,
            lastMessage.createdAt
        )
    )
    FROM Conversation c
    JOIN c.participants self
    LEFT JOIN c.participants friend ON friend.user.id <> :currentUserId
    LEFT JOIN c.lastMessage lastMessage
    WHERE c.id = :conversationId
      AND self.user.id = :currentUserId
      AND c.isGroup = false
      AND EXISTS (
          SELECT p FROM c.participants p
          WHERE p.user.id <> :currentUserId
      )
    """)
    Optional<ConversationDetailDto> findUserConversationById(
            @Param("conversationId") UUID conversationId,
            @Param("currentUserId") UUID currentUserId
    );

    @Modifying
    @Query(
            value = "UPDATE conversations SET last_message_id = :messageId WHERE id = :convoId",
            nativeQuery = true
    )
    int updateConversationLastMessage(
            @Param("convoId") UUID convoId,
            @Param("messageId") UUID messageId
    );
}

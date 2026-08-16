package com.uniread.chat.repositories;

import com.uniread.chat.domain.entities.Participant;
import com.uniread.chat.domain.entities.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

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

    Page<Conversation> findAllByParticipants(List<Participant> participants, Pageable pageable);

    @EntityGraph(attributePaths = {
            "participants",
            "participants.user",
            "participants.user.profile"
    })
    @Query("""
        SELECT c FROM Conversation c
        JOIN c.participants p
        WHERE p.user.id = :userId
        AND (p.deletedAt IS NULL
                OR c.lastMessageAt > p.deletedAt)
    """)
    Page<Conversation> findUserConversations(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
        SELECT DISTINCT c FROM Conversation c
        JOIN FETCH c.participants p
        JOIN FETCH p.user u
        JOIN FETCH u.profile
        WHERE c.id = :conversationId
    """)
    Optional<Conversation> findWithParticipantsById(@Param("conversationId") UUID conversationId);

    @Modifying
    @Query(
        value = """
            UPDATE conversations SET
            last_message_id = :messageId,
            last_message_text = :lastMessageText,
            last_message_at = :lastMessageAt,
            last_sender_name = :lastSenderName,
            last_sender_id = :lastSenderId
            WHERE id = :convoId
            """,
        nativeQuery = true
    )
    void updateConversationLastMessage(
            @Param("convoId") UUID convoId,
            @Param("messageId") UUID messageId,
            @Param("lastMessageText") String lastMessageText,
            @Param("lastMessageAt") Instant lastMessageAt,
            @Param("lastSenderName") String senderName,
            @Param("lastSenderId") UUID lastSenderId
    );
}

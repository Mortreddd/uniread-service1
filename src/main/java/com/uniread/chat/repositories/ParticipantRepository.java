package com.uniread.chat.repositories;

import com.uniread.chat.domain.entities.Participant;
import com.uniread.auth.domain.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, UUID> {

    List<Participant> findByUserIn(List<User> users);
    List<Participant> findByUser(User user);

    @EntityGraph(attributePaths = {"user", "user.profile"})
    List<Participant> findAllByUserId(UUID userId);

    @EntityGraph(attributePaths = {"user", "user.profile"})
    List<Participant> findByConversationId(UUID conversationId);

    boolean existsByConversationIdAndUserId(UUID conversationId, UUID userId);

    Boolean existsByConversationIdAndUserIdIn(UUID conversationId, List<UUID> userIds);

    @Modifying
    @Query(value = """
        UPDATE participants SET deleted_at = CURRENT_TIMESTAMP, unread_count = 0
        WHERE conversation_id = :conversationId AND user_id = :userId
        """, nativeQuery = true)
    void updateDeletedByConversationIdAndParticipantUserId(
            @Param("conversationId") UUID conversationId,
            @Param("userId") UUID userId
    );

    @Modifying
    @Query("UPDATE Participant p SET p.lastReadAt = CURRENT_TIMESTAMP, p.unreadCount = 0 " +
            "WHERE p.conversation.id = :conversationId AND p.user.id = :userId")
    int updateLastReadAtByConversationIdAndUserId(
            @Param("conversationId") UUID conversationId,
            @Param("userId") UUID userId
    );

    int deleteByUserId(UUID userId);
}

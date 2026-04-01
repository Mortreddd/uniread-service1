package com.bsit.uniread.infrastructure.repositories.message;

import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends CrudRepository<Participant, UUID> {

    List<Participant> findByUserIn(List<User> users);
    List<Participant> findByUser(User user);

    @EntityGraph(attributePaths = {"user.profile"})
    List<Participant> findByConversationId(UUID conversationId);

    boolean existsByConversationIdAndUserId(UUID conversationId, UUID userId);

    Boolean existsByConversationIdAndUserIdIn(UUID conversationId, List<UUID> userIds);
    @Modifying
    @Query("UPDATE Participant p SET p.lastReadAt = CURRENT_TIMESTAMP, p.unreadCount = 0 " +
            "WHERE p.conversation.id = :conversationId AND p.user.id = :userId")
    int updateLastReadAtByConversationIdAndUserId(
            @Param("conversationId") UUID conversationId,
            @Param("userId") UUID userId
    );

    int deleteByUserId(UUID userId);
}

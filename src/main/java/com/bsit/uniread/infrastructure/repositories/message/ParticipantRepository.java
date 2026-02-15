package com.bsit.uniread.infrastructure.repositories.message;

import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
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

    Optional<Participant> findByConversationIdAndUserId(UUID conversationId, UUID userId);
    List<Participant> findByConversation(Conversation conversation);
    List<Participant> findByIdIn(List<UUID> ids);
    Boolean existsByConversationIdAndUserId(UUID conversationId, UUID userId);

    @Modifying
    @Query(value =
            """
            UPDATE participants SET last_read_at = CURRENT_TIMESTAMP WHERE conversation_id = :conversationId AND user_id = :userId
            """,
            nativeQuery = true
    )
    void updateLastReadAtByConversationIdAndUserId(@Param("conversationId") UUID conversationId, @Param("userId") UUID userId);
}

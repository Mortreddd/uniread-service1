package com.bsit.uniread.infrastructure.repositories.message;

import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
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
public interface ConversationRepository extends JpaRepository<Conversation, UUID>, CrudRepository<Conversation, UUID> {

    List<Conversation> findByParticipants(List<Participant> participant);
    Page<Conversation> findByParticipantsUser(User user, Pageable pageable);
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

}

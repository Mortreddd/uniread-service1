package com.bsit.uniread.infrastructure.repositories.message;

import com.bsit.uniread.domain.entities.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID>,
        CrudRepository<Message, UUID> {

    @Query(
            value = """
                    SELECT unread.* 
                    FROM messages unread
                    INNER JOIN participants p ON p.conversation_id = unread.conversation_id
                    WHERE 
                        unread.created_at > p.last_read_at 
                        AND p.user_id = :receiverId
                        AND unread.conversation_id = :conversationId
                    ORDER BY unread.created_at DESC
                    LIMIT 15
                    """,
            nativeQuery = true
    )
    List<Message> findUnreadMessagesByConversationIdAndReceiverId(@Param("conversationId") UUID conversationId, @Param("receiverId") UUID receiverId);
    List<Message> findByConversationIdOrderByCreatedAtAsc(UUID conversationId);


    Page<Message> findByConversationId(UUID conversationId, Pageable pageable);
}

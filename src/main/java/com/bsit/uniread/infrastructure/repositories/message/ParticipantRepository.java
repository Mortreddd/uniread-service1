package com.bsit.uniread.infrastructure.repositories.message;

import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends CrudRepository<Participant, UUID> {

    List<Participant> findByUserIn(List<User> users);
    List<Participant> findByUser(User user);

    List<Participant> findByConversation(Conversation conversation);
    Boolean existsByConversationIdAndUserId(UUID conversationId, UUID userId);
}

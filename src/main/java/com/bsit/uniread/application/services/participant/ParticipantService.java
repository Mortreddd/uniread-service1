package com.bsit.uniread.application.services.participant;

import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.message.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    @Transactional
    public void updateLastReadAtByConversationAndUser(Conversation conversation, User user) {
        updateLastReadAtByConversationIdAndUserId(conversation.getId(), user.getId());
    }

    @Transactional
    public void updateLastReadAtByConversationIdAndUserId(UUID conversationId, UUID userId) {
        participantRepository.updateLastReadAtByConversationIdAndUserId(conversationId, userId);
    }

    @Transactional(readOnly = true)
    public Participant getParticipantByConversationAndUser(Conversation conversation, User user) {
        return participantRepository.findByConversationIdAndUserId(conversation.getId(), user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User is not found in conversation"));
    }
    /**
     * Get the list of participants based on user
     * @param user
     * @return List of Participants
     */
    @Transactional(readOnly = true)
    public List<Participant> getParticipantByUser(User user) {
        return participantRepository.findByUser(user);
    }

    /**
     * Get the participants based on users
     * @param users
     * @return list of participants
     */
    @Transactional(readOnly = true)
    public List<Participant> getParticipantsByUsers(List<User> users) {
        return participantRepository.findByUserIn(users);
    }

    /**
     * Create a participants for conversation
     * @param participants
     * @return list of participants
     */
    @Transactional
    public List<Participant> saveParticipants(List<Participant> participants) {
        return (List<Participant>) participantRepository.saveAll(participants);
    }

    /**
     * Save the participant
     * @param participant
     * @return stored participant
     */
    @Transactional
    public Participant save(Participant participant) {
        return participantRepository.save(participant);
    }

    /**
     * Save all the participants of conversations
     * @param conversation
     * @return List<Participant>
     */
    @Transactional
    public List<Participant> createParticipantsByConversation(Conversation conversation, List<User> users) {
        List<Participant> participants = conversation.getParticipants();
        participantRepository.saveAll(participants);
        return conversation.getParticipants();
    }

}

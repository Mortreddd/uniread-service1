package com.bsit.uniread.application.services.message;

import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.message.ParticipantRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

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
     * @param conversation
     * @param users
     * @return list of participants
     */
    @Transactional
    public List<Participant> createParticipants(Conversation conversation, List<User> users) {
        List<Participant> participants = users.stream()
                .map((user) -> Participant.builder()
                        .addedAt(DateUtil.now())
                        .conversation(conversation)
                        .user(user)
                        .build()
                ).toList();

        participantRepository.saveAll(participants);
        return participants;
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

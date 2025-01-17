package com.bsit.uniread.application.services.message;

import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.message.ParticipantRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    /**
     * Get the participants based on users
     * @param users
     * @return
     */
    public List<Participant> getParticipantsByUsers(List<User> users) {
        return participantRepository.findByUserIn(users);
    }

    public List<Participant> saveParticipants(Conversation conversation, List<User> users) {
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
    public List<Participant> createParticipantsByConversation(Conversation conversation, List<User> users) {
        List<Participant> participants = conversation.getParticipants();
        participantRepository.saveAll(conversation.getParticipants());
        return conversation.getParticipants();
    }

    public Boolean isParticipant(User user) {
        return participantRepository.findByUser(user)
                .isPresent();
    }
}

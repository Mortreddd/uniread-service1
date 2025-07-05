package com.bsit.uniread.infrastructure.handler.listeners.conversation;

import com.bsit.uniread.application.services.FollowService;
import com.bsit.uniread.application.services.conversation.ConversationService;
import com.bsit.uniread.application.services.message.ParticipantService;
import com.bsit.uniread.domain.entities.message.Conversation;
import com.bsit.uniread.domain.entities.message.Participant;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.follow.NewFollowerEvent;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NewMutualFollowListener implements ApplicationListener<NewFollowerEvent> {

    private final FollowService followService;
    private final ConversationService conversationService;
    private final ParticipantService participantService;

    @Override
    public void onApplicationEvent(NewFollowerEvent event) {
        User requester = event.getSender();
        User following = event.getReceiver();

        if(!followService.isMutual(requester, following)) return;

        Conversation newConversation = conversationService.save(Conversation.builder()
                .name(null)
                .isGroup(false)
                .build()
        );

        List<Participant> participants = List.of(
                Participant.builder()
                    .addedAt(DateUtil.now())
                    .user(requester)
                    .conversation(newConversation)
                    .build(),
                Participant.builder()
                    .addedAt(DateUtil.now())
                    .user(following)
                    .conversation(newConversation)
                    .build()
        );

        participantService.saveParticipants(participants);
    }
}

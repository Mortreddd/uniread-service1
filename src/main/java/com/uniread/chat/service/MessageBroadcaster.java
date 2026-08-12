package com.uniread.chat.service;

import com.uniread.chat.domain.entities.Participant;
import com.uniread.chat.dto.response.ConversationDetailDto;
import com.uniread.chat.dto.response.ConversationPreviewDto;
import com.uniread.chat.dto.response.MessageDto;
import com.uniread.chat.dto.response.ParticipantDto;
import com.uniread.common.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageBroadcaster {

    private static final String CHATS_QUEUE = "/queue/chats";
    private static final String CHATS_ON_READ_TOPIC = "/topic/chats.%s";
    private final SimpMessagingTemplate messagingTemplate;


    public void broadcastToParticipants(
        ConversationPreviewDto conversation,
        List<Participant> participants
    ) {
        participants.forEach((p) -> sendToUser(p.getUser().getId(), CHATS_QUEUE, conversation));
    }
    public void broadcastToConversation(
            ConversationPreviewDto conversation,
            MessageDto message
    ) {
        String topic = String.format(CHATS_ON_READ_TOPIC, conversation.getConversationId());
        send(topic, message);
    }

    public void broadcastTypingIndicator(UUID conversationId, UUID userId, String avatarPhoto, Boolean isTyping) {
        String topic = String.format("/topic/chats.%s.typing", conversationId);
        Map<String, Object> typingEvent = Map.of(
                "userId", userId,
                "isTyping", isTyping,
                "avatarPhoto", avatarPhoto,
                "timestamp", DateUtil.now()
        );
        messagingTemplate.convertAndSend(topic, typingEvent);
    }

    public void broadcastNewParticipantReader(UUID conversationId, UUID readerId, String avatarPhoto) {
        String topic = String.format("/topic/chats.%s.reader", conversationId);
        Map<String, Object> readerEvent = Map.of(
                "userId", readerId,
                "avatarPhoto", avatarPhoto,
                "timestamp", DateUtil.now()
        );
        messagingTemplate.convertAndSend(topic, readerEvent);
    }


    public void sendToUser(UUID userId, String destination, Object payload) {
        final String userDestination = String.format("/user/%s%s", userId, destination);
        messagingTemplate.convertAndSend(userDestination, payload);
        log.trace("Sent message to user {} on destination {}", userId, userDestination);
    }

    public void send(String destination, Object payload) {
        messagingTemplate.convertAndSend(destination, payload);
        log.trace("Sent message on destination {}", destination);
    }
}

package com.uniread.chat.service;

import com.uniread.chat.dto.response.ConversationDetailDto;
import com.uniread.chat.dto.response.MessageDto;
import com.uniread.chat.dto.response.ParticipantDto;
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

    private static final String CHAT_NOTIFICATIONS_QUEUE = "/queue/chat-notifications";
    private static final String CHAT_MESSAGES_READ_TOPIC = "/topic/chat.%s.read";


    private final SimpMessagingTemplate messagingTemplate;


    public void broadcastMessage(ConversationDetailDto conversation, MessageDto message, List<ParticipantDto> participants) {
        participants.forEach((p) -> {
           sendToUser(p.getUserId(), CHAT_NOTIFICATIONS_QUEUE, conversation);
        });
        String topic = String.format(CHAT_MESSAGES_READ_TOPIC, conversation.getConversationId());
        messagingTemplate.convertAndSend(topic, message);
        log.debug("Broadcasted message {} to conversation {}", conversation.getLastMessage().getId(), conversation.getConversationId());
    }


    public void broadcastTypingIndicator(UUID conversationId, UUID userId, Boolean isTyping) {
        String topic = String.format("/topic/chat.%s.typing", conversationId);
        Map<String, Object> typingEvent = Map.of(
                "userId", userId,
                "isTyping", isTyping,
                "timestamp", System.currentTimeMillis()
        );
        messagingTemplate.convertAndSend(topic, typingEvent);
    }

    public void broadcastNewParticipantReader(UUID conversationId, UUID readerId) {
        String topic = String.format("/topic/chat.%s.read", conversationId);
        Map<String, Object> readerEvent = Map.of(
                "userId", readerId,
                "timestamp", System.currentTimeMillis()
        );

        messagingTemplate.convertAndSend(topic, readerEvent);

    }


    public void sendToUser(UUID userId, String destination, Object payload) {
        final String userDestination = String.format("/user/%s%s", userId, destination);
        messagingTemplate.convertAndSend(userDestination, payload);
        log.trace("Sent message to user {} on destination {}", userId, userDestination);
    }
}

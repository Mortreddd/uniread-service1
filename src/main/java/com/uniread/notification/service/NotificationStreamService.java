package com.uniread.notification.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class NotificationStreamService {

    private final Map<UUID, Set<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public void send(UUID userId, Object payload) {
        var userEmitters = emitters.getOrDefault(userId, new HashSet<>());
        if (userEmitters == null || userEmitters.isEmpty()) return;

        for(var emitter : userEmitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .id(UUID.randomUUID().toString())
                        .data(payload, MediaType.APPLICATION_JSON)
                );
            } catch (IOException exception) {
                userEmitters.remove(emitter);
                emitter.completeWithError(exception);
            }
        }
        if (userEmitters.isEmpty()) {
            emitters.remove(userId);
        }

    }
    public SseEmitter subscribe(CustomUserDetails userDetails) {
        var emitter = new SseEmitter(30 * 60 * 1000L);

        Set<SseEmitter> userEmitters = emitters.computeIfAbsent(
                userDetails.getId(),
                id -> ConcurrentHashMap.newKeySet()
        );

        userEmitters.add(emitter);
        Runnable removeEmitter = () -> {
            Set<SseEmitter> current = emitters.get(userDetails.getId());
            if (current != null) {
                current.remove(emitter);
                if (current.isEmpty()) emitters.remove(userDetails.getId());
            }
        };


        emitter.onCompletion(removeEmitter);
        emitter.onTimeout(removeEmitter);
        emitter.onError(e -> removeEmitter.run());

        return emitter;
    }

}

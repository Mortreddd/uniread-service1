package com.uniread.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class ServerSentStreamService {

    private final Map<UUID, Set<SseEmitter>> emitters =
            new ConcurrentHashMap<>();

    public void send(UUID userId, Object payload) {

        Set<SseEmitter> userEmitters = emitters.get(userId);

        if (userEmitters == null || userEmitters.isEmpty()) {
            return;
        }

        for (SseEmitter emitter : userEmitters) {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("notification")
                                .data(payload, MediaType.APPLICATION_JSON)
                );

                log.debug("Notification Stream Sent: {}", payload);
            } catch (Exception exception) {
                emitter.completeWithError(exception);
                removeEmitter(userId, emitter);
            }
        }
    }

    public SseEmitter subscribe(UUID userId) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        Set<SseEmitter> userEmitters = emitters.computeIfAbsent(
                userId,
                id -> ConcurrentHashMap.newKeySet()
        );

        userEmitters.add(emitter);

        Runnable cleanup = () -> removeEmitter(userId, emitter);

        emitter.onCompletion(cleanup);
        emitter.onTimeout(() -> {
            cleanup.run();
            emitter.complete();
        });
        emitter.onError(exception -> cleanup.run());

        return emitter;
    }

    private void removeEmitter(UUID userId, SseEmitter emitter) {

        emitters.computeIfPresent(userId, (id, current) -> {
            current.remove(emitter);
            return current.isEmpty() ? null : current;
        });
    }
}
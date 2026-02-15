package com.bsit.uniread.application.services.chapter;

import com.bsit.uniread.application.dto.response.chapter.ChapterDto;
import com.bsit.uniread.application.dto.response.follow.FollowDto;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WebSocketChapterNotifier implements Broadcaster {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private void notifyFollower(UUID userId, ChapterDto chapter) {
        simpMessagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/topic/notifications",
                chapter
        );
    }

    @Override
    public void publish(Chapter chapter, List<Follow> followers) {
        followers.stream()
                .map(FollowDto::new)
                .forEach(follow -> notifyFollower(follow.getFollower().getId(), new ChapterDto(chapter)));
    }
}

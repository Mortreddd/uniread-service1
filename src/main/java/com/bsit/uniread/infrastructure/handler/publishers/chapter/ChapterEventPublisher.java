package com.bsit.uniread.infrastructure.handler.publishers.chapter;

import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.events.chapter.PublishChapterEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChapterEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publishChapter(Chapter chapter) {
        eventPublisher.publishEvent(new PublishChapterEvent(this, chapter));
    }
}

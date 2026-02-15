package com.bsit.uniread.infrastructure.security.authorizations.chapter;

import com.bsit.uniread.application.services.chapter.ChapterService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component("chapterPermission")
@RequiredArgsConstructor
public class ChapterPermission {

    private final ChapterService chapterService;

    public boolean isCurrentlyPublished(UUID chapterId) {
        Chapter chapter = chapterService.getChapterById(chapterId);
        return chapter.getIsPublished();
    }

    public boolean isAccessible(UUID bookId, UUID chapterId, CustomUserDetails userDetails) {
        Chapter chapter = chapterService.getBookChapterById(bookId, chapterId);
        if(Boolean.TRUE.equals(chapter.getIsPublished())) return true;

        /**
         * Might consider allowing collaborators of the book to access draft chapter
         */
        Book book = chapter.getBook();
        return book.getUser().getId().equals(userDetails.getId());
    }

    public boolean isBelong(UUID bookId, UUID chapterId) {
        Chapter chapter = chapterService.getChapterById(chapterId);
        return Objects.equals(chapter.getBook().getId(), bookId);
    }
}

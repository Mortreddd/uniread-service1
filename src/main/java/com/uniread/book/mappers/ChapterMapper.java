package com.uniread.book.mappers;

import com.uniread.book.domain.entities.Chapter;
import com.uniread.book.dto.response.ChapterDetailDto;
import com.uniread.book.dto.response.ChapterDto;
import org.springframework.stereotype.Component;

@Component
public class ChapterMapper {

    public ChapterDto toDto(Chapter chapter) {
        if(chapter == null) return null;

        return ChapterDto.builder()
                .id(chapter.getId())
                .volumeId(chapter.getVolume().getId())
                .title(chapter.getTitle())
                .chapterNumber(chapter.getChapterNumber())
                .status(chapter.getStatus())
                .readCount(chapter.getReadCount())
                .likesCount(chapter.getLikesCount())
                .createdAt(chapter.getCreatedAt())
                .updatedAt(chapter.getUpdatedAt())
                .deletedAt(chapter.getDeletedAt())
                .publishedAt(chapter.getPublishedAt())
                .unpublishedAt(chapter.getUnpublishedAt())
                .build();
    }

    public ChapterDetailDto toDetailDto(Chapter chapter) {
        return null;
    }
}

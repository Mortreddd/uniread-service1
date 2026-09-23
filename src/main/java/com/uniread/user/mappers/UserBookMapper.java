package com.uniread.user.mappers;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.book.domain.entities.Book;
import com.uniread.common.services.CloudinaryService;
import com.uniread.user.dto.response.UserBookDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserBookMapper {

    private final CloudinaryService cloudinaryService;

    public UserBookDetail toBookDetail(Book book) {
        var genres = book.getGenres()
                .stream()
                .map(genre -> UserBookDetail.GenreDto
                        .builder()
                        .id(genre.getId())
                        .name(genre.getName())
                        .build()
                )
                .collect(Collectors.toCollection(LinkedHashSet::new));

        var bookCoverUrl = cloudinaryService.generatePublicUrl(book.getCoverPublicId());

        return UserBookDetail.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .coverUrl(bookCoverUrl)
                .matured(book.getMatured())
                .status(book.getStatus())
                .readCount(book.getReadCount())
                .totalChapters(book.getChaptersCount())
                .totalLikes(book.getLikeCount())
                .completed(book.getCompleted())
                .genres(genres)
                .isCollaborate(book.getIsCollaborate())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();


    }
}

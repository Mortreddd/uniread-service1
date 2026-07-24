package com.uniread.book.dto.response;

import com.uniread.book.domain.entities.BookStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;
import java.util.List;

/**
 * Display the details of book
 * eg: searched book
 */
@Getter
@Setter
@Builder
public class BookDetailDto {

    private UUID id;
    private String title;
    private String description;
    private AuthorDto author;
    private Float averageRating;
    private Float totalRating;
    private Long readCount;
    private String coverPhoto;
    private Long totalLikes;
    private Long totalChapters;
    private BookStatus status;
    private Boolean completed;
    private Boolean matured;
    private List<GenreDto> genres;
    private Boolean isAddedToLibrary;
    private Instant createdAt;

}

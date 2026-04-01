package com.bsit.uniread.application.dto.response.book;

import com.bsit.uniread.application.dto.response.user.SimpleUserInfo;
import com.bsit.uniread.domain.entities.book.BookStatus;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class BookDetailsDto {

    private UUID id;
    private String title;
    private SimpleUserInfo author;
    private Float averageRating;
    private Float totalRating;
    private Long readCount;
    private String coverPhoto;
    private String description;
    private Long totalLikes;
    private Long totalChapters;
    private BookStatus status;
    private Boolean completed;
    private Boolean matured;
    private List<GenreDto> genres;
    private Boolean isAddedToLibrary;
    private Instant createdAt;

}

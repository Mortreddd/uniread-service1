package com.bsit.uniread.application.dto.response.book;

import com.bsit.uniread.application.dto.response.user.UserDto;
import com.bsit.uniread.domain.entities.book.Book;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

/**
 * Display the details of book
 * eg: searched book
 */
@Data
public class BookDetailsDto {

    private UUID id;
    private UserDto user;
    private List<GenreDto> genres;
    private String title;
    private String coverPhoto;
    private String description;
    private Integer totalChapterPublishedCount;
    private Integer totalChapterDraftsCount;
    private Integer totalChaptersCount;
    private Long totalLikesCount;
    private Long totalReadsCount;
    private Long totalRatingsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookDetailsDto(Book book) {
        this.id = book.getId();
        this.user = new UserDto(book.getUser());
        this.genres = book.getGenres().stream().map(GenreDto::new).toList();
        this.title = book.getTitle();
        this.coverPhoto = book.getCoverPhoto();
        this.description = book.getDescription();
        this.totalChapterPublishedCount = book.getTotalChapterPublishedCount();
        this.totalChapterDraftsCount = book.getTotalChapterDraftsCount();
        this.totalChaptersCount = book.getTotalChaptersCount();
        this.totalLikesCount = book.getTotalLikesCount();
        this.totalReadsCount = book.getTotalReadsCount();
        this.totalRatingsCount = book.getTotalRatingsCount();
        this.createdAt = book.getCreatedAt();
        this.updatedAt = book.getUpdatedAt();
    }

}

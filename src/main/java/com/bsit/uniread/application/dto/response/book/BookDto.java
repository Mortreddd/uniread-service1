package com.bsit.uniread.application.dto.response.book;

import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.domain.entities.book.*;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


/**
 * Full details of a book
 */
@Data
@AllArgsConstructor
public class BookDto {

    private UUID id;
    private AuthorDto user;
    private String title;
    private String coverPhoto;
    private String description;
    private BookStatus status;
    private Integer readCount;
    private Boolean completed;
    private Boolean matured;
    private Integer totalChapterPublishedCount;
    private Integer totalChapterDraftsCount;
    private Integer totalChaptersCount;
    private Long totalRatingsCount;
    private Long totalLikesCount;
    private Long totalReadsCount;

    private List<GenreDto> genres;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private LocalDateTime bannedAt;

    private LocalDateTime publishedAt;

    private List<Chapter> chapters;

    private List<BookCommentDto> bookComments;

    private List<BookLike> bookLikes;

    private List<Tag> tags;

    public BookDto(Book book) {
        this.id = book.getId();
        this.user = new AuthorDto(book.getUser());
        this.title = book.getTitle();
        this.coverPhoto = book.getCoverPhoto();
        this.description = book.getDescription();
        this.readCount = book.getReadCount();
        this.status = book.getStatus();
        this.completed = book.getCompleted();
        this.matured = book.getMatured();
        this.genres = book.getGenres().stream().map(GenreDto::new).toList();
        this.createdAt = book.getCreatedAt();
        this.updatedAt = book.getUpdatedAt();
        this.deletedAt = book.getDeletedAt();
        this.bannedAt = book.getBannedAt();
        this.publishedAt = book.getPublishedAt();
        this.chapters = book.getChapters();
        this.bookComments = book.getBookComments().stream().map(BookCommentDto::new).toList();
        this.bookLikes = book.getBookLikes();
        this.tags = book.getTags();
        this.totalChapterPublishedCount = book.getTotalChapterPublishedCount();
        this.totalChapterDraftsCount = book.getTotalChapterDraftsCount();
        this.totalChaptersCount = book.getTotalChaptersCount();
        this.totalLikesCount = book.getTotalLikesCount();
        this.totalRatingsCount = book.getTotalRatingsCount();
        this.totalReadsCount = book.getTotalReadsCount();
    }

}

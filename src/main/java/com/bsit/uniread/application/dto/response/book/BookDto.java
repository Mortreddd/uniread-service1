package com.bsit.uniread.application.dto.response.book;

import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.domain.entities.book.*;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BookDto {

    private UUID id;

    private AuthorDto user;

    private String title;
    private String coverPhoto;
    private String description;
    private Integer readCount;
    private Boolean completed;
    private Boolean matured;

    private List<GenreDto> genres;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private List<Chapter> chapters;

    private List<BookComment> bookComments;

    private List<BookLike> bookLikes;

    private List<Tag> tags;

    public BookDto(Book book) {
        this.id = book.getId();
        this.user = new AuthorDto(book.getUser());
        this.title = book.getTitle();
        this.coverPhoto = book.getCoverPhoto();
        this.description = book.getDescription();
        this.readCount = book.getReadCount();
        this.completed = book.getCompleted();
        this.matured = book.getMatured();
        this.genres = book.getGenres().stream().map(GenreDto::new).toList();
        this.createdAt = book.getCreatedAt();
        this.updatedAt = book.getUpdatedAt();
        this.deletedAt = book.getDeletedAt();
        this.chapters = book.getChapters();
        this.bookComments = book.getBookComments();
        this.bookLikes = book.getBookLikes();
        this.tags = book.getTags();
    }

}

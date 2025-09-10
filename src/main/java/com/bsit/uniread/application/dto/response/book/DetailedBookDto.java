package com.bsit.uniread.application.dto.response.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DetailedBookDto {

    private UUID id;
    private String author;
    private String title;
    private String coverPhoto;
    private String description;
    private BookStatus status;
    private Integer readCount;
    private Boolean completed;
    private Boolean matured;
    private Long totalChapterPublishedCount;
    private Long totalRatingsCount;
    private Long totalLikesCount;
    private Integer totalReadsCount;
    private Boolean isAuthor;
    private Boolean isCollaborator;
    private Boolean isSaved;

    public DetailedBookDto(Book book, BookDetailDto details) {
        this.id = book.getId();
        this.author = book.getUser().getUsername();
        this.title = book.getTitle();
        this.coverPhoto = book.getCoverPhoto();
        this.description =  book.getDescription();
        this.status = book.getStatus();
        this.readCount = book.getReadCount();
        this.completed = book.getCompleted();
        this.matured = book.getMatured();
        this.totalChapterPublishedCount = details.getTotalChapterPublishedCount();
        this.totalRatingsCount = details.getTotalRatingsCount();
        this.totalLikesCount = details.getTotalLikesCount();
        this.totalReadsCount = details.getTotalReadsCount();
        this.isAuthor = details.getIsAuthor();
        this.isCollaborator = details.getIsCollaborator();
        this.isSaved = details.getIsSaved();
    }
}

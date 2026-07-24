package com.uniread.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class BookStatsDto {
    private UUID bookId;
    private Double averageRating;
    private Long totalRating;
    private Long totalLikes;
    private Long totalChapters;
    private Boolean isAddedToLibrary;
}
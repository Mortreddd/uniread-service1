package com.uniread.user.dto.response;


import com.uniread.book.domain.entities.BookStatus;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserBookDetail {

    private UUID id;
    private String title;
    private String description;
    private Float averageRating;
    private Float totalRating;
    private Integer readCount;
    private String coverUrl;
    private Long totalLikes;
    private Integer totalChapters;
    private BookStatus status;
    private Boolean completed;
    private Boolean matured;
    private Boolean isCollaborate;
    private Set<GenreDto> genres;
    private Instant createdAt;
    private Instant updatedAt;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenreDto {
        private UUID id;
        private String name;
    }


}

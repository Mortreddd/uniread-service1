package com.bsit.uniread.application.dto.response.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDetailDto {

    private Long totalChapterPublishedCount;
    private Long totalRatingsCount;
    private Long totalLikesCount;
    private Integer totalReadsCount;
    private Boolean isAuthor;
    private Boolean isCollaborator;
    private Boolean isSaved;

}

package com.bsit.uniread.application.dto.response.book;

import com.bsit.uniread.domain.entities.book.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


/**
 * Preview details of book
 */
@Getter
@Setter
@AllArgsConstructor
public class BookDto {

    private UUID id;
    private String title;
    private String description;
    private String coverPhoto;
    private List<GenreDto> genres;
    private Float totalLikes;
    private Float readCount;
    private Float totalChapters;
    private BookStatus status;

}

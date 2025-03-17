package com.bsit.uniread.application.dto.response.book;

import com.bsit.uniread.domain.entities.book.Genre;
import lombok.Data;

@Data
public class GenreDto {
    private Integer id;
    private String name;
    private String description;

    public GenreDto(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
        this.description = genre.getDescription();
    }
}

package com.bsit.uniread.application.dto.response.book;

import com.bsit.uniread.domain.entities.book.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

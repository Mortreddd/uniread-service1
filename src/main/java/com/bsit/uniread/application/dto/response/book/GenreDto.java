package com.bsit.uniread.application.dto.response.book;

import com.bsit.uniread.domain.entities.book.Genre;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class GenreDto {
    private Integer id;
    private String name;
    private String description;
}

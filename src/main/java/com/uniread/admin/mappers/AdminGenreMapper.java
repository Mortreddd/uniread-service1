package com.uniread.admin.mappers;

import com.uniread.admin.dto.response.GenreDetailDto;
import com.uniread.book.domain.entities.Genre;
import org.springframework.stereotype.Component;

@Component
public class AdminGenreMapper {

    public GenreDetailDto toDetailDto(Genre genre) {
        if(genre == null) {
            return null;
        }

        return GenreDetailDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .description(genre.getDescription())
                .bookCount(genre.getBookCount())
                .createdAt(genre.getCreatedAt())
                .updatedAt(genre.getUpdatedAt())
                .build();
    }
}

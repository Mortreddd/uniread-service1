package com.uniread.book.mappers;

import com.uniread.book.dto.response.GenreDto;
import com.uniread.book.domain.entities.Genre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GenreMapper {

    public GenreDto toDto(Genre genre) {
        if (genre == null) {
            return null;
        }

        return new GenreDto(
                genre.getId(),
                genre.getName(),
                genre.getDescription()
        );
    }

    public List<GenreDto> toDtoList(List<Genre> genres) {
        if (genres == null) {
            return List.of();
        }
        return genres.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
package com.bsit.uniread.domain.mappers.book;

import com.bsit.uniread.application.dto.response.book.GenreDto;
import com.bsit.uniread.domain.entities.book.Genre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreDto toDto(Genre genre);
}

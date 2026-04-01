package com.bsit.uniread.application.services.book;

import com.bsit.uniread.application.dto.response.book.GenreDto;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.domain.mappers.book.GenreMapper;
import com.bsit.uniread.infrastructure.repositories.book.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreMapper mapper;
    private final GenreRepository repository;

    public List<GenreDto> getBookGenres(UUID bookId) {
        return repository.findByBookId(bookId)
                .stream()
                .map(mapper::toDto)
                .toList();

    }

    public List<GenreDto> mapToDto(List<Genre> genres) {
        return genres.stream().map(mapper::toDto).toList();
    }
}

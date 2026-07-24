package com.uniread.book.service;

import com.uniread.book.dto.response.GenreDto;
import com.uniread.book.domain.entities.Genre;
import com.uniread.book.mappers.GenreMapper;
import com.uniread.book.repositories.GenreRepository;
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
        return repository.findByBooksId(bookId)
                .stream()
                .map(mapper::toDto)
                .toList();

    }

    public List<GenreDto> mapToDto(List<Genre> genres) {
        return genres.stream().map(mapper::toDto).toList();
    }
}

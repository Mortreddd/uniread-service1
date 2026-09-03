package com.uniread.admin.services;

import com.uniread.admin.dto.request.GenreRequest;
import com.uniread.admin.dto.request.GenreMonitoringFilter;
import com.uniread.admin.dto.response.GenreDetailDto;
import com.uniread.admin.mappers.AdminGenreMapper;
import com.uniread.admin.repositories.AdminGenreRepository;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.book.domain.entities.Genre;
import com.uniread.common.exceptions.DuplicateResourceException;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.common.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminGenreService {

    private final AdminGenreMapper mapper;
    private final AdminGenreRepository genreRepository;

    public Page<GenreDetailDto> getGenres(GenreMonitoringFilter filter) {
        Pageable pageable = PageRequest.of(
                filter.getPageNo(),
                filter.getPageSize()
        );
        // Add logging for admin monitoring

        return genreRepository.findAll(pageable)
                .map(mapper::toDetailDto);
    }


    @Transactional
    public GenreDetailDto createGenre(GenreRequest request, CustomUserDetails userDetails) {

        validateGenre(request.getName());
        var genre = Genre.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        // Add logging for admin monitoring

        log.info("{} created genre {}", userDetails.getUsername(), genre.getId());

        return mapper.toDetailDto(genreRepository.save(genre));
    }


    @Transactional
    public GenreDetailDto updateGenre(UUID genreId, GenreRequest request, CustomUserDetails userDetails) {

        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find genre"));

        validateGenre(request.getName(), genreId);

        genre.setName(request.getName());
        genre.setDescription(request.getDescription());


        log.info("{} updated genre {}", userDetails.getUsername(), genre.getId());

        return mapper.toDetailDto(genreRepository.save(genre));
    }

    @Transactional
    public void deleteGenre(UUID genreId, CustomUserDetails userDetails) {

    }

    private void validateGenre(String name, UUID excludeGenreId) {
        if(name == null || name.isEmpty()) {
            throw new ValidationException("Name of genre is required");
        }

        if(genreRepository.existsByNameIgnoreCaseAndIdNot(name.trim(), excludeGenreId)) {
            throw new DuplicateResourceException("Genre already exists");
        }
    }

    private void validateGenre(String name) {
        if(name == null || name.isEmpty()) {
            throw new ValidationException("Name of genre is required");
        }

        if(genreRepository.existsByNameIgnoreCase(name.trim())) {
            throw new DuplicateResourceException("Genre already exists");
        }
    }
}

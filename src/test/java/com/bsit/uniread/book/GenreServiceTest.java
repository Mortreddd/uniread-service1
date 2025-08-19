package com.bsit.uniread.book;

import com.bsit.uniread.application.services.book.GenreService;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.infrastructure.repositories.book.GenreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @Mock
    GenreRepository genreRepository;
    @InjectMocks
    GenreService genreService;

    @AfterEach
    void tearDown() {
        genreRepository.deleteAll();
    }

    @Test
    void shouldGetGenres() {
        List<Genre> genres = List.of(
                Genre.builder()
                        .id(1)
                        .name("Mystery")
                        .description("Knowledge does not eliminate the sense of wonder and mystery. There is always more to discover.")
                        .build(),

                Genre.builder()
                        .id(2)
                        .name("Teen Fiction")
                        .description("Keep true to the dreams of your youth.")
                        .build()
        );

        Mockito.when(genreRepository.findAll()).thenReturn(genres);
        List<Genre> genreList = genreService.getGenres();
        Genre firstGenre = genreList.getFirst();
        Assertions.assertEquals(firstGenre.getName(), genres.getFirst().getName());
        Assertions.assertNotEquals(firstGenre.getName(), genres.getLast().getName());
    }

    @Test
    void shouldGetGenreById() {
        Genre toBeAdded = Genre.builder()
                .id(1)
                .name("Teen Fiction")
                .description("Keep true to the dreams of your youth")
                .build();

        Mockito.when(genreRepository.save(toBeAdded)).thenReturn(toBeAdded);
        Genre insertedGenre = genreRepository.save(toBeAdded);

        Assertions.assertEquals(1, insertedGenre.getId());
        Assertions.assertEquals("Teen Fiction", insertedGenre.getName());

        Assertions.assertNotEquals(2, insertedGenre.getId());
        Assertions.assertNotEquals("Not Existing", insertedGenre.getName());
    }


}

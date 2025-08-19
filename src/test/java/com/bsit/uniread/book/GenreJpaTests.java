package com.bsit.uniread.book;

import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.book.GenreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class GenreJpaTests {

    @Autowired
    GenreRepository genreRepository;
    List<Genre> genres;

    @BeforeEach
    void setUp() {
        this.genres = List.of(
                Genre.builder()
                        .name("Mystery")
                        .description("Knowledge does not eliminate the sense of wonder and mystery. There is always more to discover.")
                        .build(),

                Genre.builder()
                        .name("Teen Fiction")
                        .description("Keep true to the dreams of your youth.")
                        .build(),

                Genre.builder()
                        .name("Science Fiction")
                        .description("Science fiction has always served as a morality tale and will continue to do so.")
                        .build(),

                Genre.builder()
                        .name("General Fiction")
                        .description("Ultimately, fiction is the art of telling the truth through lies.")
                        .build(),

                Genre.builder()
                        .name("Historical Fiction")
                        .description("History is just a series of unexpected events. It can only get us ready to be shocked once more.")
                        .build(),

                Genre.builder()
                        .name("Fantasy")
                        .description("Fantasy isn't much of an escape from reality. It's a method of comprehending it.")
                        .build(),

                Genre.builder()
                        .name("Thriller")
                        .description("The darkest secrets are hidden behind the sweetest smiles.")
                        .build(),

                Genre.builder()
                        .name("Action")
                        .description("The little hand says it's time to rock and roll.")
                        .build(),

                Genre.builder()
                        .name("Romance")
                        .description("Read the beloved stories.")
                        .build(),

                Genre.builder()
                        .name("Adventure")
                        .description("A passport to endless adventures is reading.")
                        .build(),

                Genre.builder()
                        .name("Paranormal")
                        .description("As we gazed intently at Death's face, Death blinked first.")
                        .build(),

                Genre.builder()
                        .name("Spiritual")
                        .description("Silence is the path of spirituality.")
                        .build()

//                Genre.builder()
//                        .name("Horror")
//                        .description("Be ready to be surprised when you believe you have read everything. A brand-new collection of terrifying stories and unearthly terrors.")
//                        .build()
        );
    }


    @AfterEach
    void tearDown() {
        genreRepository.deleteAll();
    }

    @Test
    void shouldGetGenres() {
        genreRepository.saveAll(this.genres);
        List<Genre> genreList = genreRepository.findAll();

        Assertions.assertEquals(this.genres, genreList);
        Assertions.assertNotNull(this.genres);
    }

    @Test
    void shouldAddGenre() {
        Genre genre = Genre.builder()
                .name("Horror")
                .description("Be ready to be surprised when you believe you have read everything. A brand-new collection of terrifying stories and unearthly terrors.")
                .build();

        Genre fromDatabase = genreRepository.save(genre);

        Assertions.assertNotNull(fromDatabase);
        Assertions.assertEquals(genre.getName(), fromDatabase.getName());
    }

    @Test
    void shouldThrowNotFound() {
        Optional<Genre> fromDatabase = genreRepository.findByName("NonExistent");


        Assertions.assertTrue(fromDatabase.isEmpty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            genreRepository.findByName("NonExistent")
                    .orElseThrow(() -> new ResourceNotFoundException("Unable to find genre"));
        });

    }

    @Test
    void shouldNotThrowNotFound() {
        Genre genre = genreRepository.save(Genre.builder()
                .name("Science Fiction")
                .build());

        Optional<Genre> scienceFictionGenre = genreRepository.findByName("Science Fiction");

        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(scienceFictionGenre);
        Assertions.assertDoesNotThrow(() -> {
            genreRepository.findByName("Science Fiction");
        });
    }
}

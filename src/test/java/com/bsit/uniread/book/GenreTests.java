package com.bsit.uniread.book;

import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.infrastructure.repositories.book.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DataJpaTest
public class GenreTests {

    @Autowired
    private GenreRepository genreRepository;
//
//    @BeforeEach
//    public void setUp() {
//        List<Genre> genres = List.of(
//                Genre.builder()
//                        .name("Mystery")
//                        .description("Knowledge does not eliminate the sense of wonder and mystery. There is always more to discover.")
//                        .build(),
//
//                Genre.builder()
//                        .name("Teen Fiction")
//                        .description("Keep true to the dreams of your youth.")
//                        .build(),
//
//                Genre.builder()
//                        .name("Science Fiction")
//                        .description("Science fiction has always served as a morality tale and will continue to do so.")
//                        .build(),
//
//                Genre.builder()
//                        .name("General Fiction")
//                        .description("Ultimately, fiction is the art of telling the truth through lies.")
//                        .build(),
//
//                Genre.builder()
//                        .name("Historical Fiction")
//                        .description("History is just a series of unexpected events. It can only get us ready to be shocked once more.")
//                        .build(),
//
//                Genre.builder()
//                        .name("Fantasy")
//                        .description("Fantasy isn't much of an escape from reality. It's a method of comprehending it.")
//                        .build(),
//
//                Genre.builder()
//                        .name("Thriller")
//                        .description("The darkest secrets are hidden behind the sweetest smiles.")
//                        .build(),
//
//                Genre.builder()
//                        .name("Action")
//                        .description("The little hand says it's time to rock and roll.")
//                        .build(),
//
//                Genre.builder()
//                        .name("Romance")
//                        .description("Read the beloved stories.")
//                        .build(),
//
//                Genre.builder()
//                        .name("Adventure")
//                        .description("A passport to endless adventures is reading.")
//                        .build(),
//
//                Genre.builder()
//                        .name("Paranormal")
//                        .description("As we gazed intently at Death's face, Death blinked first.")
//                        .build(),
//
//                Genre.builder()
//                        .name("Spiritual")
//                        .description("Silence is the path of spirituality.")
//                        .build()
//
//                Genre.builder()
//                        .name("Horror")
//                        .description("Be ready to be surprised when you believe you have read everything. A brand-new collection of terrifying stories and unearthly terrors.")
//                        .build()
//        );
//
//        genreRepository.saveAll(genres);
//    }

//
//    @AfterEach
//    public void tearDown() {
//        genreRepository.deleteAll();
//    }

    @Test
    public void testGetGenres() {
        List<Genre> genres = genreRepository.findAll();

        assertNotNull(genres);
    }

    @Test
    public void testAddGenre() {
        Genre genre = Genre.builder()
                .name("Horror")
                .description("Be ready to be surprised when you believe you have read everything. A brand-new collection of terrifying stories and unearthly terrors.")
                .build();

        Genre fromDatabase = genreRepository.save(genre);

        assertEquals(genre, fromDatabase);
    }

    @Test
    public void testFindGenreByName() {
        Genre genre = Genre.builder()
                .name("Horror")
                .description("Be ready to be surprised when you believe you have read everything. A brand-new collection of terrifying stories and unearthly terrors.")
                .build();

        genreRepository.save(genre);

        Genre fromDatabase = genreRepository.findByName(genre.getName())
                        .orElseThrow(() -> new RuntimeException("Genre not found"));

        assertNotNull(fromDatabase);
    }
}

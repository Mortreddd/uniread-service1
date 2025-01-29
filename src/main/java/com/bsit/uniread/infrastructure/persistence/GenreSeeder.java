package com.bsit.uniread.infrastructure.persistence;

import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.infrastructure.repositories.book.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreSeeder implements CommandLineRunner {

    private final GenreRepository genreRepository;

    @Override
    public void run(String... args) throws Exception {
        String prefix = "genres/backgrounds/%s";
        if(genreRepository.count() == 0) {
            List<Genre> genres = List.of(
                    Genre.builder()
                            .name("Mystery")
                            .description("Knowledge does not eliminate the sense of wonder and mystery. There is always more to discover.")
                            .backgroundImage(String.format(prefix, "mystery.webp"))
                            .build(),

                    Genre.builder()
                            .name("Teen Fiction")
                            .description("Keep true to the dreams of your youth.")
                            .backgroundImage(String.format(prefix, "teen_fiction.webp"))
                            .build(),

                    Genre.builder()
                            .name("Science Fiction")
                            .description("Science fiction has always served as a morality tale and will continue to do so.")
                            .backgroundImage(String.format(prefix, "science_fiction.webp"))
                            .build(),

                    Genre.builder()
                            .name("General Fiction")
                            .description("Ultimately, fiction is the art of telling the truth through lies.")
                            .backgroundImage(String.format(prefix, "general_fiction.webp"))
                            .build(),

                    Genre.builder()
                            .name("Historical Fiction")
                            .description("History is just a series of unexpected events. It can only get us ready to be shocked once more.")
                            .backgroundImage(String.format(prefix, "historical_fiction.webp"))
                            .build(),

                    Genre.builder()
                            .name("Fantasy")
                            .description("Fantasy isn't much of an escape from reality. It's a method of comprehending it.")
                            .backgroundImage(String.format(prefix, "fantasy.webp"))
                            .build(),

                    Genre.builder()
                            .name("Thriller")
                            .description("The darkest secrets are hidden behind the sweetest smiles.")
                            .backgroundImage(String.format(prefix, "thriller.webp"))
                            .build(),

                    Genre.builder()
                            .name("Action")
                            .description("The little hand says it's time to rock and roll.")
                            .backgroundImage(String.format(prefix, "action.webp"))
                            .build(),

                    Genre.builder()
                            .name("Romance")
                            .description("Read the beloved stories.")
                            .backgroundImage(String.format(prefix, "romance.webp"))
                            .build(),

                    Genre.builder()
                            .name("Adventure")
                            .description("A passport to endless adventures is reading.")
                            .backgroundImage(String.format(prefix, "adventure.webp"))
                            .build(),

                    Genre.builder()
                            .name("Paranormal")
                            .description("As we gazed intently at Death's face, Death blinked first.")
                            .backgroundImage(String.format(prefix, "paranormal.webp"))
                            .build(),

                    Genre.builder()
                            .name("Spiritual")
                            .description("Silence is the path of spirituality.")
                            .backgroundImage(String.format(prefix, "spiritual.webp"))
                            .build(),
                    Genre.builder()
                            .name("Horror")
                            .description("Be ready to be surprised when you believe you have read everything. A brand-new collection of terrifying stories and unearthly terrors.")
                            .backgroundImage(String.format(prefix, "horror.webp"))
                            .build()
            );

            genreRepository.saveAllAndFlush(genres);
        }
    }
}

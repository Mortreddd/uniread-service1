package com.bsit.uniread.book;

import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.infrastructure.repositories.book.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GenreControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    GenreRepository genreRepository;

    @Test
    void shouldGetGenres() throws Exception {
        List<Genre> genres = List.of(
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
                        .build()
        );

        List<Genre> genresList = genreRepository.saveAll(genres);

        this.mockMvc.perform(get("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(genresList.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(genresList.get(1).getName()));

    }

//    @Test
    void shouldGetGenre() throws Exception {

        List<Genre> genres = genreRepository.saveAll(List.of(
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
                        .build()
        ));


        this.mockMvc.perform(get("/api/v1/genres/" + genres.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(genres.getFirst().getId()));

        this.mockMvc.perform(get("/api/v1/genres/20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

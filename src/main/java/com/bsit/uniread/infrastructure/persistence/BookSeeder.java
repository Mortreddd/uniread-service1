package com.bsit.uniread.infrastructure.persistence;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import com.bsit.uniread.infrastructure.repositories.book.GenreRepository;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookSeeder implements CommandLineRunner {

    private final ResourceLoader resourceLoader;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if(genreRepository.count() != 0 && bookRepository.count() == 0) {
            List<User> users = userRepository.findAll();
            ObjectMapper mapper = new ObjectMapper();
            try {
                var resource = resourceLoader.getResource("classpath:migrate.json");
                JsonFile jsonFile = mapper.readValue(resource.getInputStream(), JsonFile.class);
                List<Book> books = Arrays.stream(jsonFile.getResult())
                        .map((bookJson) -> {

                            List<Integer> bookIds = Arrays.stream(bookJson.getGenre())
                                    .map(Integer::valueOf)
                                    .toList();
                            List<Genre> genres = genreRepository.findAllById(bookIds);
                            User user = users.getFirst();
                            return Book.builder()
                                    .title(bookJson.getTitle())
                                    .user(user)
                                    .genres(genres)
                                    .completed(false)
                                    .matured(Boolean.valueOf(bookJson.getMatured()))
                                    .description(bookJson.getDescription())
                                    .createdAt(DateUtil.now())
                                    .readCount(Integer.valueOf(bookJson.getReadCount()))
                                    .coverPhoto(bookJson.getCoverPhoto())
                                    .build();
                        }).toList();

                bookRepository.saveAll(books);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public static class JsonFile {
        private BookJson[] result;
    }

    @Getter
    @NoArgsConstructor
    public static class BookJson {
        private String title;
        private String readCount;
        private String description;
        private String matured;
        private String coverPhoto;
        private String[] genre;
    }

}


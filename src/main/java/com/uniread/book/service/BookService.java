package com.uniread.book.service;

import com.uniread.book.dto.request.BookSearchFilter;
import com.uniread.book.dto.request.UpdateBookRequest;
import com.uniread.book.dto.response.AuthorDto;
import com.uniread.book.dto.response.BookDetailDto;
import com.uniread.book.dto.response.BookStatsDto;
import com.uniread.book.domain.entities.Book;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.book.repositories.BookRepository;
import com.uniread.book.specifications.BookSpecification;
import com.uniread.common.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final GenreService genreService;

}

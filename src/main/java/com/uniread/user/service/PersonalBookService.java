package com.uniread.user.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.book.domain.entities.Book;
import com.uniread.user.dto.request.UserBookFilter;
import com.uniread.user.dto.response.UserBookDetail;
import com.uniread.user.mappers.UserBookMapper;
import com.uniread.user.repositories.PersonalBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalBookService {

    private final UserBookMapper userBookMapper;
    private final PersonalBookRepository personalBookRepository;

    public Page<UserBookDetail> getUserBooks(UserBookFilter filter, CustomUserDetails userDetails)  {
        var pageable = PageRequest.of(
                filter.getPageNo(),
                filter.getPageSize(),
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );

        Page<UUID> bookIds = personalBookRepository.findBooksByUser(userDetails.getId(), pageable);

        if (bookIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<Book> bookWithGenres = personalBookRepository.findAllWithGenreByIds(bookIds.getContent());
        Map<UUID, Book> byId = bookWithGenres.stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));

        var books = bookIds.stream()
                .map(byId::get)
                .filter(Objects::nonNull)
                .map(userBookMapper::toBookDetail)
                .toList();


        return new PageImpl<>(books, pageable, bookIds.getTotalElements());

    }
}

package com.uniread.user.service;

import com.uniread.user.dto.request.UserProfileFilter;
import com.uniread.user.dto.response.ProfileDashboardDto;
import com.uniread.book.domain.entities.Book;
import com.uniread.book.domain.entities.BookStatus;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.book.repositories.BookRepository;
import com.uniread.user.repositories.UserRepository;
import com.uniread.book.specifications.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Page<Book> getUserBooks(CustomUserDetails userDetails, UserProfileFilter filter) {
        Sort.Direction direction = "asc".equalsIgnoreCase(filter.getSortBy()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, filter.getOrderBy());
        BookStatus status = "ALL".equalsIgnoreCase(filter.getCategory()) ? null : BookStatus.valueOf(filter.getCategory().toUpperCase());
        Specification<Book> bookSpecification = Specification
                .where(BookSpecification.hasAuthorById(userDetails.getId()))
                .and(BookSpecification.hasQuery(filter.getQuery()))
                .and(BookSpecification.hasStatus(status));

        return bookRepository.findAll(bookSpecification, PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort));
    }
}

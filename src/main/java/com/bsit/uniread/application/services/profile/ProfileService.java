package com.bsit.uniread.application.services.profile;

import com.bsit.uniread.application.dto.request.user.UserProfileFilter;
import com.bsit.uniread.application.dto.response.profile.ProfileDashboardDto;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import com.bsit.uniread.infrastructure.specifications.book.BookSpecification;
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
    public ProfileDashboardDto getUserDashboard(
            CustomUserDetails userDetails
    ) {
        return userRepository.findUserDashboard(userDetails.getId());
    }

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

package com.bsit.uniread.application.services.profile;

import com.bsit.uniread.application.dto.response.profile.ProfileDashboardDto;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import com.bsit.uniread.infrastructure.specifications.book.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "T(java.util.Objects).hash(#userDetails)")
    public ProfileDashboardDto getUserDashboard(
            CustomUserDetails userDetails
    ) {

        return userRepository.findByUserIdNative(userDetails.getId());
    }

    @Transactional(readOnly = true)
    public Page<Book> getUserBooks(
            int pageNo,
            int pageSize,
            String category,
            String query,
            String sortBy,
            String orderBy,
            String deletedAt,
            CustomUserDetails userDetails
    ) {
        Sort.Direction direction = sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, orderBy);
        BookStatus status = category.equalsIgnoreCase("ALL") ? null : BookStatus.valueOf(category.toUpperCase());
        Specification<Book> bookSpecification = Specification
                .where(BookSpecification.hasAuthorById(userDetails.getId()))
                .and(BookSpecification.hasQuery(query))
                .and(BookSpecification.hasStatus(status))
                .and(BookSpecification.hasDeleted(deletedAt));

        return bookRepository.findAll(bookSpecification, PageRequest.of(pageNo, pageSize, sort));
    }
}

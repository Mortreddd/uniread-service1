package com.bsit.uniread.application.services.user;

import com.bsit.uniread.application.dto.request.user.AuthorFilter;
import com.bsit.uniread.application.dto.response.user.AuthorDetail;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final UserRepository userRepository;

    /**
     * Get the users or authors excluding the current authenticated user
     * The authors queried that matches the query
     * @param filter
     * @param currentUserId
     * @return Pagination of Books
     */
    @Transactional(readOnly = true)
    public Page<AuthorDetail> getAuthors(
            AuthorFilter filter,
            UUID currentUserId
    ) {
        Sort.Direction direction = "asc".equalsIgnoreCase(filter.getSortBy()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, filter.getOrderBy());
        return userRepository.findAuthors(currentUserId, filter.getBookStatus(), PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort));
    }

    /**
     * Since it was for public author, assuming the book status should be published
     * @param authorId
     * @return AuthorDetails
     */
    public AuthorDetail getAuthorById(UUID authorId, BookStatus bookStatus) {
        return userRepository.findByAuthorId(authorId, bookStatus)
                .orElse(null);
    }

}

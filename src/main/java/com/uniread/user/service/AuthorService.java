package com.uniread.user.service;

import com.uniread.user.dto.request.AuthorFilter;
import com.uniread.user.dto.response.UserDetail;
import com.uniread.book.domain.entities.BookStatus;
import com.uniread.user.repositories.UserRepository;
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
    public Page<UserDetail> getUsersDetail(
            AuthorFilter filter,
            UUID currentUserId
    ) {
        Sort.Direction direction = "asc".equalsIgnoreCase(filter.getSortBy()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, filter.getOrderBy());
        var bookStatus = filter.getBookStatus();
        var pageable = PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort);

        return userRepository.findUsersDetail(currentUserId, bookStatus, pageable);
    }

    /**
     * Since it was for public author, assuming the book status should be published
     * @param authorId
     * @return AuthorDetails
     */
    public UserDetail getUserDetailById(UUID authorId, BookStatus bookStatus) {
        return userRepository.findUserDetailById(authorId, bookStatus)
                .orElse(null);
    }

}

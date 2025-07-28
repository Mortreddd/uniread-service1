package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.application.services.user.AuthorService;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * User endpoint allowed for guest users
 * @url /api/v1/authors
 */
@RequiredArgsConstructor
@RequestMapping(path = ApiEndpoints.AUTHORS)
@RestController
public class AuthorController {

    private final AuthorService authorService;
    private final FollowRepository followRepository;

    @GetMapping
    public ResponseEntity<Page<AuthorDto>> getAuthors(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "sortBy", defaultValue = "asc", required = false) String sortBy,
            @RequestParam(name = "orderBy", defaultValue = "createdAt", required = false) String orderBy,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "bannedAt", required = false) String bannedAt,
            @RequestParam(name = "deletedAt", required = false) String deletedAt,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<User> users = authorService.getAuthors(pageNo, pageSize, query, sortBy, orderBy, startDate, endDate, bannedAt, deletedAt, userDetails);
        List<Follow> currentUserFollowings = userDetails == null ? List.of() : followRepository.findByFollowingIdOrFollowerId(userDetails.getId(), userDetails.getId());
        Page<AuthorDto> authors = users.map(u -> {
                UUID authorId = u.getId();

                boolean isFollowing = false;
                boolean isMutualFollow = false;

                if (userDetails != null) {
                    UUID userId = userDetails.getId();
                    isFollowing = currentUserFollowings.stream()
                            .anyMatch(f -> f.getFollower().getId().equals(userId) && f.getFollowing().getId().equals(authorId));

                    isMutualFollow = currentUserFollowings.stream()
                            .anyMatch(f -> f.getFollower().getId().equals(authorId) && f.getFollowing().getId().equals(userId));
                }

                var author = new AuthorDto(u);
                author.setIsFollowing(isFollowing);
                author.setIsMutualFollowing(isFollowing && isMutualFollow);
                return author;
            });
        return ResponseEntity.ok().body(authors);
    }

    @GetMapping(path = "/{authorId}")
    public ResponseEntity<AuthorDto> getAuthorById(
            @PathVariable(name = "authorId") UUID authorId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AuthorDto author = new AuthorDto(authorService.getAuthorById(authorId));
        List<Follow> follows = userDetails == null ? List.of() : followRepository.findByFollowingIdOrFollowerId(userDetails.getId(), userDetails.getId());
        boolean isFollowing = false;
        boolean isMutualFollowing = false;
        if(userDetails != null) {
            isFollowing = follows.stream()
                    .anyMatch(f ->
                            f.getFollower().getId().equals(userDetails.getId()) && f.getFollowing().getId().equals(author.getId()));

            isMutualFollowing = follows.stream()
                    .anyMatch(f -> f.getFollower().getId().equals(author.getId()) && f.getFollowing().getId().equals(userDetails.getId()));
        }

        author.setIsMutualFollowing(isMutualFollowing);
        author.setIsFollowing(isFollowing);
        return ResponseEntity.ok().body(author);
    }

    @GetMapping(path = "/{userId}/books")
    public ResponseEntity<Page<BookDto>> getAuthorsBook(
            @PathVariable(name = "userId") UUID userId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "0") int pageSize,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "status", required = false) BookStatus status,
            @RequestParam(name = "sortBy", defaultValue = "asc", required = false) String sortBy,
            @RequestParam(name = "orderBy", defaultValue = "createdAt", required = false) String orderBy
    ) {

        Page<BookDto> books = authorService.getAuthorBooksById(userId, pageNo, pageSize, query, status, sortBy, orderBy).map(BookDto::new);
        return ResponseEntity.ok()
                .body(books);

    }


}

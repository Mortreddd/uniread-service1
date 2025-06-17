package com.bsit.uniread.application.controllers.reaction;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.reaction.BookCommentReactionRequest;
import com.bsit.uniread.application.dto.response.reaction.BookCommentLikeDto;
import com.bsit.uniread.application.services.reaction.BookCommentReactionService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = ApiEndpoints.BOOK_COMMENTS_REACTIONS)
public class BookCommentReactionController {


    private final UserService userService;
    private final BookCommentReactionService bookCommentReactionService;

    @PostMapping(path = "/create")
    public ResponseEntity<BookCommentLikeDto> createCommentReaction(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "commentId") UUID commentId,
            @RequestBody BookCommentReactionRequest request,
            @AuthenticationPrincipal User userPrincipal
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.error("Authentication is NULL in SecurityContextHolder");
        } else {
            log.info("Authentication class: {}", authentication.getClass().getName());
            log.info("Principal class: {}", authentication.getPrincipal().getClass().getName());
            log.info("Is authenticated: {}", authentication.isAuthenticated());
            log.info("Authorities: {}", authentication.getAuthorities());

            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                log.info("UserDetails username: {}", userDetails.getUsername());
            }
        }

        if (userPrincipal == null) {
            log.error("@AuthenticationPrincipal is NULL");
        } else {
            log.info("@AuthenticationPrincipal user: {}", userPrincipal.getUsername());
        }
//        BookCommentLikeDto like = new BookCommentLikeDto(
//                bookCommentReactionService.createReaction(bookId, commentId, user, request.getReaction())
//        );
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(like);

        return null;
    }


    @DeleteMapping(path = "/reactions/{bookCommentLikeId}")
    public ResponseEntity<SuccessResponse> removeReaction(
            @PathVariable(name = "bookCommentLikeId") UUID bookCommentLikeId,
            Authentication authentication
    ) {
        bookCommentReactionService.removeReaction(bookCommentLikeId, authentication);
        SuccessResponse response = SuccessResponse
                .builder()
                .code(HttpStatus.OK.value())
                .message("Successfully removed reaction")
                .build();

        return ResponseEntity.ok()
                .body(response);
    }
}

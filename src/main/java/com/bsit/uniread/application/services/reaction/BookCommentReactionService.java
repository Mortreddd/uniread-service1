package com.bsit.uniread.application.services.reaction;

import com.bsit.uniread.application.services.comment.BookCommentService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.Reaction;
import com.bsit.uniread.domain.entities.book.BookComment;
import com.bsit.uniread.domain.entities.book.BookCommentLike;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.handler.exceptions.reaction.AlreadyReactedException;
import com.bsit.uniread.infrastructure.handler.exceptions.reaction.UnmatchedReactionException;
import com.bsit.uniread.infrastructure.repositories.book.BookCommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookCommentReactionService {

    private final UserService userService;
    private final BookCommentService bookCommentService;
    private final BookCommentLikeRepository bookCommentLikeRepository;


    @Transactional(readOnly = true)
    public BookCommentLike getBookCommentLikeById(UUID bookCommentLikeId) {
        return bookCommentLikeRepository.findById(bookCommentLikeId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find user reaction"));
    }


    public BookCommentLike createReaction(
            UUID bookId,
            UUID bookCommentId,
            User reactor,
            Reaction reaction
    ) {
        BookComment comment = bookCommentService.getBookCommentById(bookCommentId);

        if(alreadyReacted(reactor, comment)) {
            throw new AlreadyReactedException("Unable to save repeated reaction");
        }

        BookCommentLike bookCommentLike = BookCommentLike
                .builder()
                .reaction(reaction)
                .user(reactor)
                .bookComment(comment)
                .build();

        return save(bookCommentLike);
    }

    @Transactional(readOnly = true)
    public boolean alreadyReacted(User user, BookComment bookCommentLike) {
        return bookCommentLikeRepository.existsByUserAndBookComment(user, bookCommentLike);
    }

    @Transactional(readOnly = true)
    public BookCommentLike save(BookCommentLike bookCommentLike) {
        return bookCommentLikeRepository.save(bookCommentLike);
    }


    @Transactional
    public void removeReaction(UUID bookCommentLikeId, Authentication authentication) {

        BookCommentLike reaction = getBookCommentLikeById(bookCommentLikeId);
        User currentUser = (User) authentication.getPrincipal();

        if(!Objects.equals(reaction.getUser(), currentUser)) {
            throw new UnmatchedReactionException("Unable to delete reaction of user " + currentUser.getId());
        }

        bookCommentLikeRepository.delete(reaction);
    }

}

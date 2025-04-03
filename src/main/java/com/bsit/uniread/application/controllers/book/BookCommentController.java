package com.bsit.uniread.application.controllers.book;

import com.bsit.uniread.application.constants.ApiEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Api endpoint - /api/v1/books/{bookId}/comments
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = ApiEndpoints.BOOK_COMMENTS)
public class BookCommentController {

    /**
     * Get the comments of book based on bookId
     * @param bookId
     * @param pageNo
     * @param pageSize
     * @return pagination of book comments
     */
    @GetMapping
    public ResponseEntity<String> getBookComments(
            @PathVariable(name = "bookId") UUID bookId,
            @RequestParam(name = "pageNo", required = false) int pageNo,
            @RequestParam(name = "pageSize", required = false) int pageSize
    ) {

        return ResponseEntity.ok().body(bookId.toString());

    }
}

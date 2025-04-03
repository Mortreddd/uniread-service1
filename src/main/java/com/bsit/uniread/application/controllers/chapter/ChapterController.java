package com.bsit.uniread.application.controllers.chapter;

import com.bsit.uniread.application.constants.ApiEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Api endpoint - /api/v1/books/{bookId}/chapters
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = ApiEndpoints.BOOK_CHAPTERS)
public class ChapterController {

}

package com.uniread.book.controllers;

import com.uniread.book.dto.request.BookSearchFilter;
import com.uniread.book.dto.response.BookDetailDto;
import com.uniread.book.service.BookService;
import com.uniread.auth.domain.entities.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping(path = "/books")
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

}

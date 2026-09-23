package com.uniread.book.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/{bookId}/chapters")
@RestController
@RequiredArgsConstructor
public class ChapterController {



}

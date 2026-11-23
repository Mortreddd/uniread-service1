package com.uniread.book.service;

import com.uniread.book.dto.request.CreateBookRequest;
import com.uniread.common.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookValidator {

    public void validateBookCreate(CreateBookRequest request) {
        if(request == null) {
            throw new ValidationException("Unable to proceed with the request");
        }

        if(request.getCover() == null) {
            throw new ValidationException("Book cover is required");
        }

        if(request.getTitle().isBlank()) {
            throw new ValidationException("Title of the book is required");
        }

        if(request.getDescription().isBlank()) {
            throw new ValidationException("Synopsis of the book is required");
        }

        if(request.getGenres() == null || request.getGenres().isEmpty()) {
            throw new ValidationException("Book genre must contain at least 1 genre");
        }

        if(request.getMatured() == null) {
            throw new ValidationException("Book mature content must not be null");
        }
    }
}

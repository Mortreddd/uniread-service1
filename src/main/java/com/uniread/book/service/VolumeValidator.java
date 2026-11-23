package com.uniread.book.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.book.dto.request.CreateVolumeRequest;
import com.uniread.book.repositories.BookRepository;
import com.uniread.book.repositories.VolumeRepository;
import com.uniread.common.exceptions.AccessDeniedException;
import com.uniread.common.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VolumeValidator {

    private final BookRepository bookRepository;
    private final VolumeRepository volumeRepository;

    public void validateCreate(UUID bookId, CreateVolumeRequest request, CustomUserDetails userDetails) {
        if(request == null) {
            throw new ValidationException("Unable to create volume");
        }
        if(request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ValidationException("Volume title is required");
        }

        if(request.getVolumeNumber() == null) {
            throw new ValidationException("Volume number is required");
        }

        // Might consider allowing the collaborator soon
        var isAllowed = bookRepository.existsByIdAndUserId(bookId, userDetails.getId());
        if(!isAllowed) {
            throw new AccessDeniedException("Not allowed to perform action");
        }

        var hasEmptyChapters = volumeRepository.hasEmptyChapters(bookId);
        if(hasEmptyChapters) {
            throw new ValidationException("Unable to add new volume with empty chapters");
        }
    }
}

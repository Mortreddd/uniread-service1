package com.uniread.book.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.book.dto.request.CreateChapterRequest;
import com.uniread.book.repositories.VolumeRepository;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.common.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChapterValidator {

    private final VolumeRepository volumeRepository;

    public void validateCreate(
            UUID volumeId,
            CreateChapterRequest request,
            CustomUserDetails userDetails
    ) {
        if(request == null) {
            throw new ValidationException("Unable to process the chapter creation");
        }

        var isExisting = volumeRepository.existsById(volumeId);
        if(!isExisting) throw new ResourceNotFoundException("The volumes does not exists");

    }
}

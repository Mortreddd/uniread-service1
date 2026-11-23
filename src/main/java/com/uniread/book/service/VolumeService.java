package com.uniread.book.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.book.domain.entities.Book;
import com.uniread.book.domain.entities.Volume;
import com.uniread.book.domain.entities.VolumeStatus;
import com.uniread.book.dto.request.BookVolumeFilter;
import com.uniread.book.dto.request.CreateVolumeRequest;
import com.uniread.book.dto.response.VolumeDto;
import com.uniread.book.mappers.VolumeMapper;
import com.uniread.book.repositories.BookRepository;
import com.uniread.book.repositories.VolumeRepository;
import com.uniread.book.specifications.VolumeSpecification;
import com.uniread.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VolumeService {

    private final VolumeValidator validator;
    private final BookRepository bookRepository;
    private final VolumeRepository volumeRepository;
    private final VolumeMapper mapper;

    public Page<VolumeDto> getBookVolumes(
            UUID bookId,
            BookVolumeFilter filter,
            CustomUserDetails userDetails
    ) {

        var book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Unable to get the book"));

        UUID userId = userDetails != null
                ? userDetails.getId()
                : null;

        boolean isOwner = userId != null
                && Objects.equals(userId, book.getUser().getId());

        VolumeStatus visibleStatus = null;

        if (!isOwner) {
            if (!book.isPublished()) {
                return Page.empty(
                        PageRequest.of(
                                filter.getPageNo(),
                                filter.getPageSize()
                        )
                );
            }

            visibleStatus = VolumeStatus.PUBLISHED;
        }

        Specification<Volume> specification = Specification.allOf(
                VolumeSpecification.hasBook(bookId),
                VolumeSpecification.hasStatus(visibleStatus)
        );

        var pageable = PageRequest.of(
                filter.getPageNo(),
                filter.getPageSize(),
                Sort.by(
                        Sort.Direction.ASC,
                        "volumeNumber"
                )
        );

        return volumeRepository
                .findAll(specification, pageable)
                .map(mapper::toDto);
    }

    public VolumeDto getBookVolume(
            UUID volumeId,
            CustomUserDetails userDetails
    ) {
        var publicStatus = VolumeStatus.PUBLISHED;
        var volume = volumeRepository.findByIdAndStatus(volumeId, publicStatus)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find volume"));


        return mapper.toDto(volume);
    }

    @Transactional
    public VolumeDto createBookVolume(
            UUID bookId,
            CustomUserDetails userDetails,
            CreateVolumeRequest request
    ) {

        validator.validateCreate(bookId, request, userDetails);

        var volume = Volume.builder()
                .book(Book.builder().id(bookId).build())
                .volumeNumber(request.getVolumeNumber())
                .title(request.getTitle())
                .description(request.getTitle())
                .status(VolumeStatus.DRAFT)
                .chaptersCount(0)
                .build();

        return mapper.toDto(volumeRepository.save(volume));
    }
}

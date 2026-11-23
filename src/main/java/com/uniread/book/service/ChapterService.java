package com.uniread.book.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.book.domain.entities.Chapter;
import com.uniread.book.domain.entities.ChapterStatus;
import com.uniread.book.domain.entities.Volume;
import com.uniread.book.dto.request.ChapterFilter;
import com.uniread.book.dto.request.CreateChapterRequest;
import com.uniread.book.dto.response.ChapterDto;
import com.uniread.book.mappers.ChapterMapper;
import com.uniread.book.repositories.ChapterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChapterService {

    private final ChapterMapper mapper;
    private final ChapterValidator validator;
    private final ChapterRepository chapterRepository;

    public Page<ChapterDto> getChapters(
            UUID volumeId,
            ChapterFilter filter,
            CustomUserDetails userDetails
    ) {

        var isPublicUser = userDetails == null; // Also include not owner of the book or has access to the book

        var pageable = PageRequest.of(
                filter.getPageNo(),
                filter.getPageSize(),
                Sort.by(Sort.Direction.ASC, "chapterNumber")
        );

        if(isPublicUser) {
            var publicStatus = ChapterStatus.PUBLISHED;

            return chapterRepository.findByVolumeIdAndStatus(volumeId, publicStatus, pageable)
                    .map(mapper::toDto);
        }

        return chapterRepository.findByVolumeId(volumeId, pageable)
                .map(mapper::toDto);
    }


    public ChapterDto createChapter(
            UUID volumeId,
            CreateChapterRequest request,
            CustomUserDetails userDetails
    ) {
        validator.validateCreate(volumeId, request, userDetails);

        var chapter = Chapter.builder()
                .volume(Volume.builder().id(volumeId).build())
                .title(request.getTitle())
                .chapterNumber(request.getChapterNumber())
                .status(ChapterStatus.DRAFT)
                .readCount(0L)
                .likesCount(0L)
                .build();


        var created = chapterRepository.save(chapter);

        return mapper.toDto(created);
    }
}

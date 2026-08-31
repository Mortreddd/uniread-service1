package com.uniread.admin.services;

import com.uniread.admin.dto.request.GenreMonitoringFilter;
import com.uniread.admin.dto.request.TagMonitoringFilter;
import com.uniread.admin.dto.request.TagRequest;
import com.uniread.admin.dto.response.GenreDetailDto;
import com.uniread.admin.dto.response.TagDetailDto;
import com.uniread.admin.mappers.AdminTagMapper;
import com.uniread.admin.repositories.AdminTagRepository;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.book.domain.entities.Tag;
import com.uniread.common.exceptions.DuplicateResourceException;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.common.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminTagService {

    private final AdminTagMapper mapper;
    private final AdminTagRepository tagRepository;

    public Page<TagDetailDto> getTags(TagMonitoringFilter filter) {
        Pageable pageable = PageRequest.of(
                filter.getPageNo(),
                filter.getPageSize()
        );
        // Add logging for admin monitoring

        return tagRepository.findAll(pageable)
                .map(mapper::toDetailDto);
    }

    @Transactional
    public TagDetailDto createTag(TagRequest request, CustomUserDetails userDetails) {
        validateName(request.getName());

        var tag = Tag.builder()
                .name(request.getName())
                .build();

        log.info("{} created tag {}", userDetails.getUsername(), tag.getId());
        return mapper.toDetailDto(tagRepository.save(tag));

    }

    @Transactional
    public TagDetailDto updateTag(UUID tagId, TagRequest request, CustomUserDetails userDetails) {

        var tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find tag name"));


        validateName(request.getName(), tag.getId());
        tag.setName(request.getName());

        log.info("{} updated tag {}", userDetails.getUsername(), tag.getId());

        return mapper.toDetailDto(tagRepository.save(tag));

    }


    private void validateName(String tagName) {
        if(tagName == null || tagName.isEmpty()) {
            throw new ValidationException("Tag name is required");
        }

        if(tagRepository.existsByNameIgnoreCase(tagName)) {
            throw new DuplicateResourceException("Tag name already exists");
        }
    }

    private void validateName(String tagName, UUID tagId) {
        if(tagName == null || tagName.isEmpty()) {
            throw new ValidationException("Tag name is required");
        }

        if(tagRepository.existsByNameIgnoreCaseAndIdNot(tagName, tagId)) {
            throw new DuplicateResourceException("Tag name already exists");
        }
    }



}

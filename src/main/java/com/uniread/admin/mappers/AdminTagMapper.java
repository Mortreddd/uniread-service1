package com.uniread.admin.mappers;

import com.uniread.admin.dto.response.TagDetailDto;
import com.uniread.book.domain.entities.Tag;
import org.springframework.stereotype.Component;

@Component
public class AdminTagMapper {

    public TagDetailDto toDetailDto(Tag tag) {
        if(tag == null) return null;


        return TagDetailDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .usageCount(tag.getUsageCount())
                .createdAt(tag.getCreatedAt())
                .updatedAt(tag.getUpdatedAt())
                .build();
    }
}

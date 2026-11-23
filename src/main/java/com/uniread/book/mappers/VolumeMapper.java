package com.uniread.book.mappers;

import com.uniread.book.domain.entities.Volume;
import com.uniread.book.dto.response.VolumeDto;
import org.springframework.stereotype.Component;

@Component
public class VolumeMapper {

    public VolumeDto toDto(Volume volume) {
        if(volume == null) return null;
        return VolumeDto.builder()
                .id(volume.getId())
                .bookId(volume.getBook().getId())
                .volumeNumber(volume.getVolumeNumber())
                .title(volume.getTitle())
                .description(volume.getDescription())
                .coverUrl(volume.getCoverUrl())
                .coverPublicId(volume.getCoverPublicId())
                .status(volume.getStatus())
                .chaptersCount(volume.getChaptersCount())
                .createdAt(volume.getCreatedAt())
                .updatedAt(volume.getUpdatedAt())
                .deletedAt(volume.getDeletedAt())
                .publishedAt(volume.getPublishedAt())
                .build();
    }

}

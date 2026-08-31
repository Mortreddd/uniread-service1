package com.uniread.admin.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GenreDetailDto {

    private UUID id;
    private String name;
    private String description;
    private Long bookCount;
    private Instant createdAt;
    private Instant updatedAt;
}

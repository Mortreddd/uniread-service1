package com.uniread.admin.dto.response;


import lombok.*;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TagDetailDto {
    private UUID id;
    private String name;
    private Long usageCount;
    private Instant createdAt;
    private Instant updatedAt;
}

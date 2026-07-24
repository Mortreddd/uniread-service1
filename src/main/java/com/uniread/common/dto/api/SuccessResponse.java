package com.uniread.common.dto.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SuccessResponse {

    private Integer code;
    private String message;

}

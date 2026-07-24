package com.uniread.common.dto.api;

import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private Map<String, List<String>> errors;      // Field-specific errors with multiple messages
    private Map<String, String> fieldErrors;       // Field-specific errors with single message
    private List<String> globalErrors;             // Global/non-field errors
}
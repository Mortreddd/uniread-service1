package com.uniread.common.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageValidator {

    void validateFile(MultipartFile file);
}

package com.uniread.common.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.uniread.auth.domain.entities.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Map upload(byte[] fileBytes, Map<String, Object> uploadOptions) {
        try {
            return cloudinary.uploader().upload(fileBytes, uploadOptions);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to upload image", exception);
        }
    }

    public void delete(String publicId) {
        if(publicId == null || publicId.isBlank()) return;
        try {
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }

    public String generateUrl(String publicId, Integer width, Integer height) {
        if (publicId == null || publicId.isEmpty()) {
            return null;
        }

        return cloudinary.url()
                .transformation(new Transformation()
                        .width(width != null ? width : 200)
                        .height(height != null ? height : 200)
                        .crop("fill")
                        .gravity("face")
                        .quality("auto:best")
                        .fetchFormat("webp"))
                .generate(publicId);
    }

    public String generatePublicUrl(String publicId) {
        return cloudinary
                .url()
                .secure(true)
                .generate(publicId);
    }

    public String generatePrivateUrl(String publicId) {
        return cloudinary.url()
                .secure(true)
                .signed(true)
                .generate(publicId);
    }
}

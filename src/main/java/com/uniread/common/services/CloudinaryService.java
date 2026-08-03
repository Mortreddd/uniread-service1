package com.uniread.common.services;

import com.cloudinary.Cloudinary;
import com.uniread.auth.domain.entities.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;


    public Map<String, Object> generateSignature(CustomUserDetails userDetails, String type) {
        long timestamp = System.currentTimeMillis() / 1000;
        String folder = switch(type) {
            case "avatar" -> "uniread/avatars/" + userDetails.getId().toString();
            case "userCover" -> "uniread/user-covers/" + userDetails.getId().toString();
            case "bookCover" -> "uniread/book-covers/" + userDetails.getId().toString();
            default -> throw new IllegalArgumentException("Invalid type");

        };


        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("timestamp", timestamp);
        paramsToSign.put("folder", folder);

        String signature = cloudinary.apiSignRequest(
                paramsToSign,
                cloudinary.config.apiSecret,
                1
        );

        return Map.of(
                "signature", signature,
                "timestamp", timestamp,
                "apiKey", cloudinary.config.apiKey,
                "cloudName", cloudinary.config.cloudName,
                "folder", folder
        );
    }

    public void delete(String publicId) {
        if(publicId == null || publicId.isBlank()) return;
        try {
            log.debug("Deleting asset {}", publicId);
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }
}

package com.uniread.user.service;

import com.cloudinary.Transformation;
import com.uniread.common.exceptions.ValidationException;
import com.uniread.common.services.CloudinaryService;
import com.uniread.common.services.PrivateImageService;
import com.uniread.common.services.PublicImageService;
import com.uniread.common.utils.DateUtil;
import com.uniread.common.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfilePhotoService implements PublicImageService, PrivateImageService {

    private final CloudinaryService cloudinaryService;

    private static final String AVATAR_FOLDER = "uniread/avatars/%s_%s";
    private static final String COVER_FOLDER = "uniread/covers/%s_%s";
    private static final int MAX_IMAGE_SIZE = 10 * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");


    public void deleteAvatar(String publicId) {
        cloudinaryService.delete(publicId);
    }

    public void deleteCoverPhoto(String publicId) {
        cloudinaryService.delete(publicId);
    }

    /**
     * Delete old avatar and upload new one
     */
    public Map<String, Object> replaceAvatar(MultipartFile file, UUID userId, String oldPublicId) {
        var uploadResult = uploadProfileImage(file, userId);

        if (oldPublicId != null && !oldPublicId.isEmpty()) {
            deleteAvatar(oldPublicId);
        }

        return uploadResult;
    }

    /**
     * Delete old cover photo and upload new one
     */
    public Map<String, Object> replaceCover(MultipartFile file, UUID userId, String oldPublicId) {
        var uploadResult = uploadCoverPhoto(file, userId);

        if (oldPublicId != null && !oldPublicId.isEmpty()) {
            deleteCoverPhoto(oldPublicId);
        }

        return uploadResult;
    }

    public Map<String, Object> uploadCoverPhoto(MultipartFile file, UUID userId) {
        validateFile(file);

        try {
            String publicId = String.format(AVATAR_FOLDER, userId, UUID.randomUUID());

            Map<String, Object> uploadOptions = new HashMap<>();
            uploadOptions.put("public_id", publicId);
            uploadOptions.put("overwrite", true);
            uploadOptions.put("invalidate", true);

            uploadOptions.put("transformation", new Transformation<>()
                    .width(1200).height(1000).crop("fill")
                    .gravity("face")
                    .quality("auto:best")
                    .fetchFormat("webp")
                    .chain()
                    .effect("sharpen")
            );

            Map uploadResult = cloudinaryService.upload(file.getBytes(), uploadOptions);

            String publicIdResult = uploadResult.get("public_id").toString();

            log.info("Avatar uploaded successfully for user {}: {}", userId, publicIdResult);
            return uploadOptions;

        } catch (IOException e) {
            log.error("Failed to upload avatar for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to upload avatar", e);
        }
    }

    public Map<String, Object> uploadProfileImage(MultipartFile file, UUID userId) {
        validateFile(file);

        try {
            String publicId = String.format(AVATAR_FOLDER, userId, UUID.randomUUID());

            Map<String, Object> uploadOptions = new HashMap<>();
            uploadOptions.put("public_id", publicId);
            uploadOptions.put("overwrite", true);
            uploadOptions.put("invalidate", true);

            uploadOptions.put("transformation", new Transformation<>()
                    .width(400).height(400).crop("fill")
                    .gravity("face")
                    .quality("auto:best")
                    .fetchFormat("webp")
                    .chain()
                    .effect("sharpen")
            );

            Map uploadResult = cloudinaryService.upload(file.getBytes(), uploadOptions);

            String publicIdResult = uploadResult.get("public_id").toString();

            log.info("Cover Photo uploaded successfully for user {}: {}", userId, publicIdResult);
            return uploadOptions;

        } catch (IOException e) {
            log.error("Failed to upload cover photo for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to upload avatar", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new ValidationException("Image size exceeds maximum allowed (10MB)");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ValidationException("File must be an image");
        }

        String extension = FileUtil.getExtension(file);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new ValidationException("Invalid image format. Allowed: JPG, PNG, WebP");
        }
    }

    @Override
    public String generateUrl(String publicId) {
        return cloudinaryService.generatePublicUrl(publicId);
    }

    @Override
    public String generateSignedUrl(String publicId) {
        return cloudinaryService.generatePrivateUrl(publicId);
    }

    @Override
    public String generateUrl(String publicId, Integer width, Integer height) {
        return "";
    }

    @Override
    public String generateSignedUrl(String publicId, Integer width, Integer height) {
        return "";
    }

    @Override
    public String generatePrivateUrl(String publicId) {
        return "";
    }

    @Override
    public String generatePrivateSignedUrl(String publicId) {
        return "";
    }

    @Override
    public String generatePrivateUrl(String publicId, Integer width, Integer height) {
        return "";
    }

    @Override
    public String generatePrivateSignedUrl(String publicId, Integer width, Integer height) {
        return "";
    }
}

package com.uniread.book.service;

import com.cloudinary.Transformation;
import com.uniread.common.exceptions.ValidationException;
import com.uniread.common.services.CloudinaryService;
import com.uniread.common.services.ImageValidator;
import com.uniread.common.services.PrivateImageService;
import com.uniread.common.services.PublicImageService;
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
public class BookCoverService implements PublicImageService, PrivateImageService, ImageValidator {


    private final CloudinaryService cloudinaryService;

    private static final String BOOK_FOLDER = "uniread/books/%s_%s";
    private static final int MAX_IMAGE_SIZE = 10 * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");


    public void deleteCover(String publicId) {
        cloudinaryService.delete(publicId);
    }

    /**
     * Delete old avatar and upload new one
     */
    public Map<String, Object> replaceAvatar(MultipartFile file, UUID bookId, String oldPublicId) {
        var uploadResult = uploadCoverPhoto(file, bookId);

        if (oldPublicId != null && !oldPublicId.isEmpty()) {
            deleteCover(oldPublicId);
        }

        return uploadResult;
    }

    public Map<String, Object> uploadCoverPhoto(MultipartFile file, UUID bookId) {
        validateFile(file);

        try {
            String publicId = String.format(BOOK_FOLDER, bookId, UUID.randomUUID());

            Map<String, Object> uploadOptions = new HashMap<>();
            uploadOptions.put("public_id", publicId);
            uploadOptions.put("overwrite", true);
            uploadOptions.put("invalidate", true);

            uploadOptions.put("transformation", new Transformation<>()
                    .width(600).height(900).crop("fill")
                    .gravity("face")
                    .quality("auto:best")
                    .fetchFormat("webp")
                    .chain()
                    .effect("sharpen")
            );

            Map uploadResult = cloudinaryService.upload(file.getBytes(), uploadOptions);

            String publicIdResult = uploadResult.get("public_id").toString();

            log.info("Book cover uploaded successfully for book {}: {}", bookId, publicIdResult);
            return uploadOptions;

        } catch (IOException e) {
            log.error("Failed to upload book cover for book {}: {}", bookId, e.getMessage());
            throw new RuntimeException("Failed to upload book cover", e);
        }
    }


    @Override
    public void validateFile(MultipartFile file) {
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

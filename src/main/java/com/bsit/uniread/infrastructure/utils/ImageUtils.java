package com.bsit.uniread.infrastructure.utils;

import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * The current code is only supports development and may change in the future
 */
@Component
public class ImageUtils {


    @Value("${file.uploads}")
    private String fileUploadsDir;

    /**
     * Save the image and retrieves the fileName of the created file
     * @param directory
     * @param file
     * @return fileName
     * @throws IOException
     */
    public String saveImage(ImageDirectory directory, MultipartFile file) throws IOException {
        String extension = getExtension(file.getOriginalFilename());
        String generatedName = generateRandomName(16) + "." + extension;

        String subDir = getDirectory(directory);
        Path uploadPath = Paths.get(fileUploadsDir, subDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(generatedName);
        try {

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return UrlUtil.getInstance().getBaseUrl() + "/" + fileUploadsDir + "/" + subDir + "/" + generatedName;
        } catch (Exception e) {
            Files.deleteIfExists(filePath);
            throw new ResourceNotFoundException("Unable to save the book");
        }
    }

    public Boolean deleteImage(String path) throws IOException {
        String filePath = extractFileDirectory(path);

        return Files.deleteIfExists(Path.of(filePath));
    }

    private String getExtension(String originalName) {
        if(originalName == null || !originalName.contains(".")) {
            return "jpeg";
        }

        return originalName.substring(originalName.lastIndexOf(".") + 1);
    }
    /**
     * Extract the directory from the given enum ImageDirectory
     * @param directory
     * @return directory
     */
    private String getDirectory(ImageDirectory directory) {
        return switch(directory) {
            case COVER -> "cover";
            case AVATAR -> "avatar";
        };
    }

    private String extractFileDirectory(String filePath) {
        return filePath.substring(filePath.lastIndexOf(fileUploadsDir));
    }

    /**
     * Generate random string for file name
     * @param n
     * @return filename
     */
    private String generateRandomName(int n) {
        return StringUtils.randomAlphanumeric(n);
    }
}

package com.bsit.uniread.infrastructure.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

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

        Path uploadPath = Paths.get(fileUploadsDir);
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UrlUtil.getInstance().getBaseUrl() + "/" + getDirectory(directory) + "/" + new Date().toString();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
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
}

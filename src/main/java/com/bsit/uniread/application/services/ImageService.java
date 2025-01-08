package com.bsit.uniread.application.services;

import com.bsit.uniread.domain.exceptions.ResizeImageException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class ImageService {

    public byte[] resizeImage(InputStream imageInputStream, int width, int height) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(imageInputStream)
                    .size(width, height)
                    .outputFormat("JPEG")
                    .outputQuality(1)
                    .toOutputStream(outputStream);
            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new ResizeImageException("Unable to resize the profile photo");
        }


    }

    public byte[] resizeImage(InputStream imageInputStream, int width) {
        int height = 150;
        return resizeImage(imageInputStream, width, height);
    }

    public byte[] resizeImage(InputStream imageInputStream) {
        int height = 150;
        int width = 150;
        return resizeImage(imageInputStream, width, height);
    }
}

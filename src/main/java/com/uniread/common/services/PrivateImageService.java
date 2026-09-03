package com.uniread.common.services;

public interface PrivateImageService {


    String generatePrivateUrl(String publicId);

    String generatePrivateSignedUrl(String publicId);

    String generatePrivateUrl(String publicId, Integer width, Integer height);

    String generatePrivateSignedUrl(String publicId, Integer width, Integer height);
}

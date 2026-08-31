package com.uniread.common.services;

public interface PublicImageService {

    String generateUrl(String publicId);

    String generateSignedUrl(String publicId);

    String generateUrl(String publicId, Integer width, Integer height);

    String generateSignedUrl(String publicId, Integer width, Integer height);
}
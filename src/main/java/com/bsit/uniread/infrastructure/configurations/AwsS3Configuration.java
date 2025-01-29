package com.bsit.uniread.infrastructure.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class AwsS3Configuration {

    // The access key for accessing aws
    @Value("${aws.s3.access.key}")
    private String accessKey;

    // The secret key for accessing aws
    @Value("${aws.s3.secret.key}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials()))
                .build();
    }

    private AwsCredentials getCredentials() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }
}

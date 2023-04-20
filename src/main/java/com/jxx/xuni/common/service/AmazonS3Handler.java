package com.jxx.xuni.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Handler {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String putS3Object(MultipartFile multipartFile) throws IOException {

        if (multipartFile == null) {
            return "";
        }
        String objectKey = createObjectKey(multipartFile);

        try {
            PutObjectRequest putObjectRequest = createPutObjectRequest(multipartFile, "image/" + objectKey);

            InputStream inputStream = multipartFile.getInputStream();
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, multipartFile.getSize()));

            inputStream.close();
            return objectKey;

        } catch (S3Exception e) {
            log.warn("S3Exception {}", e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return "";
    }

    private String createObjectKey(MultipartFile multipartFile) {
        String uuidStoreFileName = createUUID();

        String ext = extractExt(multipartFile.getOriginalFilename());

        return uuidStoreFileName + ext;
    }

    private PutObjectRequest createPutObjectRequest(MultipartFile multipartFile, String objectKey) {

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(multipartFile.getContentType()) // META-DATA Content-Type 설정
                .acl(ObjectCannedACL.PUBLIC_READ) // 읽기 권한 - 버킷이 아니라 오브젝트 ACL 설정
                .build();

        return putObjectRequest;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos);
    }

    private String createUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}

package com.gabozago.backend.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gabozago.backend.user.domain.ProfileImage;
import com.gabozago.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile) throws IOException {

        try {
            String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(multipartFile.getInputStream().available());

            amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

            return amazonS3.getUrl(bucket, s3FileName).toString();
        } catch (Exception e){
            log.error(e.getMessage());
            return e.getMessage();
        }
    }

    public ProfileImage uploadProfileImage(MultipartFile multipartFile, User user) {
        try {
            String extension = Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.")[1];
            String s3FilePath = "uploads/user/profiles/" + user.getId() + "/" + UUID.randomUUID() + "." + extension;

            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(multipartFile.getInputStream().available());

            amazonS3.putObject(bucket, s3FilePath, multipartFile.getInputStream(), objMeta);

            return ProfileImage.builder()
                    .fileName(UUID.randomUUID() + "." + extension)
                    .size(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .path("/" + s3FilePath)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

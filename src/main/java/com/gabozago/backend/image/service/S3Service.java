package com.gabozago.backend.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
}

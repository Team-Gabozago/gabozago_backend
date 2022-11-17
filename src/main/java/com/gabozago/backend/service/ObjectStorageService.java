package com.gabozago.backend.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ObjectStorageService {

    private final AmazonS3 s3;

    @Value("${cloud.ncp.objectStorage.uploadImages.bucket}")
    private String bucketName;


    // 1. 로컬에 파일 생성(MultipartFile을 File로 변환)
    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        return convFile;
    }

    // 2. Object Storage에 파일 업로드
    public String upload(MultipartFile multipartFile, String fileName) {

        String url;

        // https://guide.ncloud-docs.com/docs/storage-storage-8-1#%EB%A9%80%ED%8B%B0%ED%8C%8C%ED%8A%B8%EC%97%85%EB%A1%9C%EB%93%9C
        // 멀티 파트 업로드
        try {

            File file = convert(multipartFile);

            long contentLength = file.length();
            long partSize = 10 * 1024 * 1024;

            // initialize and get upload ID
            InitiateMultipartUploadResult initiateMultipartUploadResult = s3.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, fileName));
            String uploadId = initiateMultipartUploadResult.getUploadId();

            // upload parts
            List<PartETag> partETagList = new ArrayList<PartETag>();

            long fileOffset = 0;
            for (int i = 1; fileOffset < contentLength; i++) {
                partSize = Math.min(partSize, (contentLength - fileOffset));

                log.info("fileOffset: {}",fileOffset,"partSize: {}",partSize);

                UploadPartRequest uploadPartRequest = new UploadPartRequest()
                        .withBucketName(bucketName)
                        .withKey(fileName)
                        .withUploadId(uploadId)
                        .withPartNumber(i)
                        .withFile(file)                 // file을 나눠서 보냄
                        .withFileOffset(fileOffset)
                        .withPartSize(partSize);

                UploadPartResult uploadPartResult = s3.uploadPart(uploadPartRequest);
                partETagList.add(uploadPartResult.getPartETag());

                fileOffset += partSize;
            }

            url = s3.getUrl(bucketName,fileName).toString();

            // complete
            CompleteMultipartUploadResult completeMultipartUploadResult = s3.completeMultipartUpload(new CompleteMultipartUploadRequest(bucketName, fileName, uploadId, partETagList));

            removeNewFile(file);
            return url;

        } catch (AmazonS3Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        } catch(SdkClientException e) {
            log.error(e.getMessage());
            return e.getMessage();
        } catch (IOException e) {
            log.error(e.getMessage());
            return e.getMessage();
        }

    }

    // 3. 로컬에 생성된 파일삭제
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }
}

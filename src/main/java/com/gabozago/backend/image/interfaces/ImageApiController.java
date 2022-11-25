package com.gabozago.backend.image.interfaces;

import com.gabozago.backend.error.ErrorCode;
import com.gabozago.backend.error.ErrorResponse;
import com.gabozago.backend.image.service.ObjectStorageService;
import com.gabozago.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ImageApiController {

    @Value("${cloud.ncp.objectStorage.uploadImages.bucket}")
    private String imageBucket;
    private final ObjectStorageService objectStorageService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@AuthenticationPrincipal User user, @RequestParam("file") MultipartFile multipartFile) throws IOException {

        int extIdx = multipartFile.getOriginalFilename().lastIndexOf(".");
        String extension = multipartFile.getOriginalFilename().substring(extIdx+1);
        String fileName = UUID.randomUUID() + "." + extension;

        String url = objectStorageService.upload(multipartFile,fileName);
        if (!url.endsWith(extension)){
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.FAIL_TO_UPLOAD_IMAGES),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(url, HttpStatus.OK);

    }
}

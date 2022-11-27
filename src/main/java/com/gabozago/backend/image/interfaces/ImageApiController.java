package com.gabozago.backend.image.interfaces;

import com.gabozago.backend.error.ErrorCode;
import com.gabozago.backend.error.ErrorResponse;
import com.gabozago.backend.image.interfaces.dto.ImageResponse;
import com.gabozago.backend.image.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ImageApiController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile multipartFile) throws IOException {

        String url = s3Service.upload(multipartFile);

        if (!url.startsWith("https")){
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.CANNOT_UPLOAD_IMAGE), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(new ImageResponse(url));

    }
}

package com.gabozago.backend.image.service;

import com.gabozago.backend.image.config.ObjectStorageMockConfig;
import io.findify.s3mock.S3Mock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
//import static org.junit.Assert.assertTrue; //Junit4

@Slf4j
@Import(ObjectStorageMockConfig.class)
@SpringBootTest
public class ObjectStorageServiceTest {

    @Autowired
    private S3Mock s3Mock;
    @Autowired
    private ObjectStorageService objectStorageService;

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    @Test
    void upload() throws IOException {
        // given
        String fileName = "test.png";
        String contentType = "image/png";
        String dirName = "test";

        MockMultipartFile multipartFile = new MockMultipartFile("test", fileName, contentType, "test".getBytes());

        // when
        String url = objectStorageService.upload(multipartFile,fileName);
        log.info("url: {}",url);

        // then
        //assertTrue(url.endsWith("png"));
        //assertTrue(url.startsWith("https"));

        // Object storage에서 test.png 지우기
    }
}

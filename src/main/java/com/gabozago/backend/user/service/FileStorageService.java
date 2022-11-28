package com.gabozago.backend.user.service;

import com.gabozago.backend.user.domain.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    void newDirectory();

    void save(MultipartFile file, User user);

    Resource load(String filename);
}

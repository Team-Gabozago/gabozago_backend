package com.gabozago.backend.user.service;

import com.gabozago.backend.user.domain.ProfileImage;
import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.user.infrastructure.ProfileImageRepository;
import com.gabozago.backend.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private final Path rootLocation = Paths.get("uploads").toAbsolutePath().normalize();

    private final ProfileImageRepository profileImageRepository;

    private final UserRepository userRepository;

    @Override
    public void newDirectory() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }

    @Override
    public void save(MultipartFile file, User user) {
        if (!Files.exists(rootLocation)) {
            newDirectory();
        }

        try {
            String extension = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
            String uuid = java.util.UUID.randomUUID().toString();
            String newFileName = uuid + "." + extension;

            Files.copy(file.getInputStream(), this.rootLocation.resolve(newFileName));

            ProfileImage profileImage = ProfileImage.builder()
                    .fileName(newFileName)
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .path(rootLocation.toString())
                    .build();

            profileImageRepository.save(profileImage);

            user.updateProfileImage(profileImage);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new org.springframework.core.io.UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}

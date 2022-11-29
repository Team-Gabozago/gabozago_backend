package com.gabozago.backend.user.interfaces;

import com.gabozago.backend.common.exception.ImageNotSavedException;
import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.user.interfaces.dto.*;
import com.gabozago.backend.user.service.FileStorageService;

import com.gabozago.backend.user.service.FavoriteService;
import com.gabozago.backend.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final FileStorageService storageService;

    private final FavoriteService favoriteService;

    private final ProfileService profileService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ProfileResponse.of(
                user,
                favoriteService.getFavoritesByUserId(user.getId()),
                favoriteService.getAllCategories()
        ));
    }

    @PatchMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileUpdateResponse> update(@AuthenticationPrincipal User user, final @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        profileService.update(user, profileUpdateRequest);

        return ResponseEntity.ok(ProfileUpdateResponse.of("profile updated"));
    }

    @PostMapping(value = "/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileImageUploadResponse> uploadProfileImage(@AuthenticationPrincipal User user, @RequestParam("image") MultipartFile image) {
        try {
            storageService.save(image, user);
            return ResponseEntity.ok(
                    ProfileImageUploadResponse.of(user.getProfileImage())
            );
        } catch (Exception e) {
            throw new ImageNotSavedException(e.getMessage());
        }
    }

    @GetMapping(value = "/images/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getProfileImage(@AuthenticationPrincipal User user, @PathVariable String filename) {
        if (user.getProfileImage() == null && Objects.equals(user.getProfileImage().getFileName(), filename)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(storageService.load(filename));
    }

    @PatchMapping(value = "/favorites/{categoryId:[\\d]+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileUpdateFavoritesResponse> updateFavorites(@AuthenticationPrincipal User user, @PathVariable Long categoryId) {
        favoriteService.addFavorite(user, categoryId);

        return ResponseEntity.ok(ProfileUpdateFavoritesResponse.of("favorite updated"));
    }

    @DeleteMapping(value = "/favorites/{categoryId:[\\d]+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileDeleteFavoritesResponse> deleteFavorites(@AuthenticationPrincipal User user, @PathVariable Long categoryId) {
        favoriteService.deleteFavorite(user, categoryId);
        return ResponseEntity.ok(ProfileDeleteFavoritesResponse.of("favorite deleted"));
    }

    @DeleteMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileDeleteResponse> leave(@AuthenticationPrincipal User user) {
        profileService.leave(user);

        return ResponseEntity.ok(ProfileDeleteResponse.of("profile deleted"));
    }

    @GetMapping(value = "/feeds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileFeedsResponse> feeds(@AuthenticationPrincipal User user)
    {
        return ResponseEntity.ok(profileService.getFeedsByUser(user));
    }

    @GetMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileCommentsResponse> comments(@AuthenticationPrincipal User user)
    {
        return ResponseEntity.ok(profileService.getCommentsByUser(user));
    }

    @GetMapping(value = "/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileLikesResponse> favorites(@AuthenticationPrincipal User user)
    {
        return ResponseEntity.ok(profileService.getLikesByUser(user));
    }
}
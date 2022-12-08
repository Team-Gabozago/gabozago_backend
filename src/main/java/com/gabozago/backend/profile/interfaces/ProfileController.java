package com.gabozago.backend.profile.interfaces;

import com.gabozago.backend.common.exception.ImageNotSavedException;
import com.gabozago.backend.image.service.S3Service;
import com.gabozago.backend.profile.domain.ProfileImage;
import com.gabozago.backend.profile.interfaces.dto.*;
import com.gabozago.backend.user.domain.User;

import com.gabozago.backend.profile.service.FavoriteService;
import com.gabozago.backend.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final FavoriteService favoriteService;

    private final ProfileService profileService;

    private final S3Service s3Service;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FindResponse> find(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(FindResponse.of(
                user,
                favoriteService.getFavoritesByUserId(user.getId()),
                favoriteService.getAllCategories()
        ));
    }

    @PatchMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateResponse> update(@AuthenticationPrincipal User user, final @Valid @RequestBody UpdateRequest profileUpdateRequest) {
        profileService.update(user, profileUpdateRequest);

        return ResponseEntity.ok(UpdateResponse.of("profile updated"));
    }

    @DeleteMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteResponse> leave(@AuthenticationPrincipal User user) {
        profileService.leave(user);

        return ResponseEntity.ok(DeleteResponse.of("profile deleted"));
    }

    @PostMapping(value = "/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UploadProfileImageResponse> uploadProfileImage(@AuthenticationPrincipal User user, @RequestParam("image") MultipartFile image) {
        try {
            ProfileImage profileImage = s3Service.uploadProfileImage(image, user);

            return ResponseEntity.ok(profileService.updateProfileImage(profileImage, user));
        } catch (Exception e) {
            throw new ImageNotSavedException(e.getMessage());
        }
    }

    @PatchMapping(value = "/favorites/{categoryId:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateFavoritesResponse> updateFavorites(@AuthenticationPrincipal User user, @PathVariable Long categoryId) {
        favoriteService.addFavorite(user, categoryId);

        return ResponseEntity.ok(UpdateFavoritesResponse.of("favorite updated"));
    }

    @DeleteMapping(value = "/favorites/{categoryId:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteFavoritesResponse> deleteFavorites(@AuthenticationPrincipal User user, @PathVariable Long categoryId) {
        favoriteService.deleteFavorite(user, categoryId);
        return ResponseEntity.ok(DeleteFavoritesResponse.of("favorite deleted"));
    }

    @GetMapping(value = "/feeds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetFeedsResponse> feeds(@AuthenticationPrincipal User user)
    {
        return ResponseEntity.ok(profileService.getFeedsByUser(user));
    }

    @GetMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetCommentsResponse> comments(@AuthenticationPrincipal User user)
    {
        return ResponseEntity.ok(profileService.getCommentsByUser(user));
    }

    @GetMapping(value = "/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetLikesResponse> favorites(@AuthenticationPrincipal User user)
    {
        return ResponseEntity.ok(profileService.getLikesByUser(user));
    }
}
package com.gabozago.backend.user.interfaces.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.user.domain.ProfileImage;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileImageUploadResponse {
    private String message;
    private String code;
    private String imageUrl;

    public ProfileImageUploadResponse(String message, String code, String url) {
        this.message = message;
        this.code = code;
        this.imageUrl = url;
    }

    public static ProfileImageUploadResponse of(ProfileImage profileImage) {
        return new ProfileImageUploadResponse(
                "이미지가 업로드되었습니다.",
                "IMAGE_UPLOADED",
                "https://api.wonto.site/profile/images/" + profileImage.getFileName());
    }
}

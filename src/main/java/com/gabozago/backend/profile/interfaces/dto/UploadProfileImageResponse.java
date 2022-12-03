package com.gabozago.backend.profile.interfaces.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.profile.domain.ProfileImage;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UploadProfileImageResponse {
    private final String message;
    private final String code;
    private final String imageUrl;

    public UploadProfileImageResponse(String message, String code, String url) {
        this.message = message;
        this.code = code;
        this.imageUrl = url;
    }

    public static UploadProfileImageResponse of(ProfileImage profileImage) {
        return new UploadProfileImageResponse(
                "이미지가 업로드되었습니다.",
                "IMAGE_UPLOADED",
                "https://wontu-images.s3.ap-northeast-2.amazonaws.com" + profileImage.getPath());
    }
}

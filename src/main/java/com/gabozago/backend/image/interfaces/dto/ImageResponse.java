package com.gabozago.backend.image.interfaces.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ImageResponse {

    private String url;

    @Builder
    public ImageResponse(String url){
        this.url = url;
    }
}

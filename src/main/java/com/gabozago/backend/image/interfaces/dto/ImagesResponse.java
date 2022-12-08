package com.gabozago.backend.image.interfaces.dto;


import com.gabozago.backend.feed.domain.Image;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ImagesResponse {

    private final Long id;

    private final String filePath;

    public ImagesResponse(Long id, String filePath) {
        this.id = id;
        this.filePath = filePath;
    }

    public static List<ImagesResponse> toList(List<Image> images) {
        return images.stream()
                .filter(image -> image.getState().equals("ACTIVE"))
                .map(image -> new ImagesResponse(image.getId(), image.getFilePath()))
                .collect(Collectors.toList());
    }
}

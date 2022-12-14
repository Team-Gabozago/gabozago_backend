package com.gabozago.backend.feed.interfaces.dto;

import com.gabozago.backend.feed.domain.Category;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.domain.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FeedRequest {

    @NotNull(message = "카테고리는 빈 값일 수 없습니다.")
    private Long categoryId;

    @NotBlank(message = "제목은 빈 값일 수 없습니다.")
    private String title;

    @NotBlank(message = "내용은 빈 값일 수 없습니다.")
    private String content;

    @NotNull(message = "위도는 빈 값일 수 없습니다.")
    private Double longitude;

    @NotNull(message = "경도는 빈 값일 수 없습니다.")
    private Double latitude;

    private String place;

    private String placeDetail;

    private List<String> images = new ArrayList<>();

    public FeedRequest(Long categoryId, String title, String content,
                       Double longitude, Double latitude, String place, String placeDetail,
            List<String> images) {
        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
        this.longitude = longitude;
        this.latitude = latitude;
        this.place = place;
        this.placeDetail = placeDetail;
        this.images = images;
    }

    public Feed toEntity(Category category) {
        return Feed.builder()
                .category(category)
                .title(this.title)
                .content(this.content)
                .location(new Location(this.longitude, this.latitude, this.place, this.placeDetail))
                .build();
    }
}

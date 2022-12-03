package com.gabozago.backend.user.interfaces.dto;

import com.gabozago.backend.user.domain.Area;
import lombok.Getter;

@Getter
public class UserAreaResponse {
    private final String name;
    private final String latitude;
    private final String longitude;

    public UserAreaResponse(String name, String latitude, String longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static UserAreaResponse of(Area area) {
        return new UserAreaResponse(area.getName(), String.valueOf(area.getLatitude()), String.valueOf(area.getLongitude()));
    }
}

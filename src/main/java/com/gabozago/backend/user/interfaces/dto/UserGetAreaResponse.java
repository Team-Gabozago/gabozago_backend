package com.gabozago.backend.user.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserGetAreaResponse {
    private final String message;
    private final String code;

    private final String regionType;
    private final String regionCode;
    private final String addressName;
    private final String region1depthName;
    private final String region2depthName;
    private final String region3depthName;
    private final String region4depthName;
    private final double x;
    private final double y;

    public UserGetAreaResponse(String message, Map<String, Object> area) {
        this.message = message;
        this.code = "AREA_FETCHED";

        this.regionType = Objects.requireNonNull((String) area.get("region_type"));
        this.regionCode = Objects.requireNonNull((String) area.get("code"));
        this.addressName = Objects.requireNonNull((String) area.get("address_name"));
        this.region1depthName = Objects.requireNonNull((String) area.get("region_1depth_name"));
        this.region2depthName = Objects.requireNonNull((String) area.get("region_2depth_name"));
        this.region3depthName = Objects.requireNonNull((String) area.get("region_3depth_name"));
        this.region4depthName = Objects.requireNonNull((String) area.get("region_4depth_name"));
        this.x = (double) area.get("x");
        this.y = (double) area.get("y");
    }

    public static UserGetAreaResponse of(String message, Map<String, Object> area) {
        return new UserGetAreaResponse(message, area);
    }
}

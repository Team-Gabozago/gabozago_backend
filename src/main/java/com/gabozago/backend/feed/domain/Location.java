package com.gabozago.backend.feed.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Location {

    private double longitude;

    private double latitude;

    private String place;

    private String placeDetail;

    protected Location() {

    }

    public Location(double longitude, double latitude, String place, String placeDetail) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.place = place;
        this.placeDetail = placeDetail;
    }
}

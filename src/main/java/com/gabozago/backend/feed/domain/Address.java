package com.gabozago.backend.feed.domain;


import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private double longitude;
    private double latitude;


    protected Address() {

    }

    public Address(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}

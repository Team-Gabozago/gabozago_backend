package com.gabozago.backend.feed.interfaces.dto;

import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class RecentRequestParams {

    @Pattern(regexp = "^[1-9][0-9]*$")
    private String nextFeedId = "10000000";

    @Pattern(regexp = "^[1-9][0-9]*$")
    private String countPerPage = "15";

    public long getNextFeedId() {
        return Long.parseLong(nextFeedId);
    }

    public int getCountPerPage() {
        return Integer.parseInt(countPerPage);
    }
}

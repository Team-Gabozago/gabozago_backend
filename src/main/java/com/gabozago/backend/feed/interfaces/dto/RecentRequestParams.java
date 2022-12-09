package com.gabozago.backend.feed.interfaces.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class RecentRequestParams {

    private String categories = "";

    private String keyword = "";

    private String sortType = "";

    @Pattern(regexp = "^[1-9][0-9]*$")
    private String nextFeedId = "10000000";

    @Pattern(regexp = "^[1-9][0-9]*$")
    private String countPerPage = "15";

    public String getKeyword() {
        return keyword;
    }

    public String getCategories() {
        return categories;
    }

    public String getSortType() {
        return sortType;
    }

    public long getNextFeedId() {
        return Long.parseLong(nextFeedId);
    }

    public int getCountPerPage() {
        return Integer.parseInt(countPerPage);
    }
}

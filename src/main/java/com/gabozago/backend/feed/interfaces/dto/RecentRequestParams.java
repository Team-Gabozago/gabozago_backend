package com.gabozago.backend.feed.interfaces.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class RecentRequestParams {

    private String categoryName = "";

    @Pattern(regexp = "^[1-9][0-9]*$")
    private String nextFeedId = "10";

    @Pattern(regexp = "^[1-9][0-9]*$")
    private String countPerPage = "10";


    private String sortType = "NEWEST";

    public String getCategories() {
        return categoryName;
    }

    public long getNextFeedId() {
        return Long.parseLong(nextFeedId);
    }

    public int getCountPerPage() {
        return Integer.parseInt(countPerPage);
    }
}

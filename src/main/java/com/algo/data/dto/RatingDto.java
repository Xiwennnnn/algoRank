package com.algo.data.dto;

import lombok.Data;

@Data
public class RatingDto extends BaseDto implements Comparable<RatingDto> {
    protected String userName;
    protected Integer rating;

    @Override
    public int compareTo(RatingDto o) {
        return rating - o.rating;
    }
}

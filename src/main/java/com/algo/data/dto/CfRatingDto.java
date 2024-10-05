package com.algo.data.dto;

import lombok.Data;

@Data
public class CfRatingDto extends RatingDto {
    private String rank;
    private Integer maxRating;

    @Override
    public String toString() {
        return "CfRatingDto{" +
                "rank='" + rank + '\'' +
                ", highestRating=" + maxRating +
                ", userName='" + userName + '\'' +
                ", rating=" + rating +
                '}';
    }
}

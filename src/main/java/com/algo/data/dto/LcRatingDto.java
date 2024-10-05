package com.algo.data.dto;

import lombok.Data;

@Data
public class LcRatingDto extends RatingDto {
    private String grading;
    private Integer nationRank;
    private double topPercentage;

    @Override
    public String toString() {
        return "LcRatingDto{" +
                "userName='" + userName + '\'' +
                ", rating=" + rating +
                ", grading='" + grading + '\'' +
                ", nationRank=" + nationRank +
                ", topPercentage=" + topPercentage +
                '}';
    }
}

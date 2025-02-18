package com.algo.data.dto.codeforces;

import lombok.Data;

@Data
public class CfRatingChangeDto {
    private Integer contestId;
    private String contestName;
    private String handle;
    private Integer rank;
    private Long ratingUpdateTimeSeconds;
    private Integer oldRating;
    private Integer newRating;
}

package com.algo.data.dto.leetcode;

import com.algo.data.dto.RatingDto;
import lombok.Data;

@Data
public class LcRatingDto extends RatingDto {
    private String realName;
    private String grading;
    private Integer nationRank;
    private Integer globalRank;
    private double topPercentage;
    private Integer globalTotalParticipants;
    private Integer localTotalParticipants;
    private String AvatarUrl;
}

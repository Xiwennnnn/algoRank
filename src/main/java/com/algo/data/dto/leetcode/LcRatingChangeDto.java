package com.algo.data.dto.leetcode;

import lombok.Data;

@Data
public class LcRatingChangeDto {
    private String handle;
    private String title;
    private boolean attended;
    private int totalProblems;
    private String trendingDirection;
    private long finishTimeInSeconds;
    private int rating;
    private int score;
    private int ranking;
    private long startTime;
}

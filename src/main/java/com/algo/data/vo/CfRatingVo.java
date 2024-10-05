package com.algo.data.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CfRatingVo implements Serializable {
    private Long cfId;
    private String userName;
    private String realName;
    private String grade;
    private String major;
    private Integer rating;
    private String ratingView;
    private String cfRank;
    private Integer maxRating;
    private String maxRatingView;
}

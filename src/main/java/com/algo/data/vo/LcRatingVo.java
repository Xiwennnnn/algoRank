package com.algo.data.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LcRatingVo implements Serializable {
    private Long lcId;
    private String userName;
    private String realName;
    private String grade;
    private String major;
    private Integer rating;
    private String grading;
    private Integer nationRank;
    private Double topPercentage;
}

package com.algo.data.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("lc_rating")
public class LcRatingDo {
    @TableId
    private Long lcId;
    private String userName;
    private Integer rating;
    private String grading;
    private Integer nationRank;
    private Double topPercentage;
}

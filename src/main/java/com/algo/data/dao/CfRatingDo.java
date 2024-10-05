package com.algo.data.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("cf_rating")
public class CfRatingDo {
    @TableId
    private Long cfId;
    private String userName;
    private Integer rating;
    private String cfRank;
    private Integer maxRating;
}

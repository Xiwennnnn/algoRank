package com.algo.data.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("algo_user")
public class AlgoUserDo {
    @TableId
    private Long userId;
    private String realName;
    private String grade;
    private String major;
    private Long lcId;
    private Long cfId;
}

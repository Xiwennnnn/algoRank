package com.algo.data.dao;

import com.algo.data.dto.ContestDto;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("contest")
@Data
public class ContestDo implements Comparable<ContestDo> {
    @TableId
    private int contestId;
    private String oj;
    private String name;
    @JsonFormat(timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(timezone = "GMT+8")
    private Date endTime;
    private String status;
    private boolean oiContest;
    private String link;

    @Override
    public int compareTo(ContestDo o) {
        int cmp = startTime.compareTo(o.startTime);
        if (cmp == 0) {
            cmp = oj.compareTo(o.oj);
        }
        if (cmp == 0) {
            cmp = name.compareTo(o.name);
        }
        return cmp;
    }
}

package com.algo.data.dao;

import com.algo.data.dto.ContestDto;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("contest")
@Data
public class ContestDo implements Comparable<ContestDo>, Serializable {
    @TableId(value = "contest_id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "oj")
    private String oj;
    @TableField(value = "name")
    private String name;
    @TableField(value = "start_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "GMT+8")
    private Date startTime;
    @TableField(value = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "GMT+8")
    private Date endTime;
    @TableField(value = "status")
    private String status;
    @TableField(value = "oi_contest")
    private boolean oiContest;
    @TableField(value = "link")
    private String link;
    @TableField(value = "is_notify")
    private boolean isNotify;

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

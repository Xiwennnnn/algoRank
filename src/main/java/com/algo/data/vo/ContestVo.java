package com.algo.data.vo;

import lombok.Data;

@Data
public class ContestVo {
    private Long id;
    private String oj;
    private String name;
    private String startTime;
    private String duration;
    private String status;
    private Boolean isToday;
}

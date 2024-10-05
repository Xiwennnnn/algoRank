package com.algo.data.dto;

import com.algo.data.common.ContestStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ContestDto extends BaseDto implements Comparable<ContestDto> {
    private String oj;
    private String name;
    private Date startTime;
    private Long startTimeStamp;
    private Date endTime;
    private Long endTimeStamp;
    private String status;
    private boolean oiContest;
    private String link;

    public ContestDto(String oj, String name, Date startTime, Date endTime, String status, String link) {
        this(oj, name, startTime, endTime, status, false, link);
    }

    public ContestDto(String oj, String name, Date startTime, Date endTime, String status, boolean oiContest, String link) {
        this.oj = oj;
        this.name = name;
        this.startTime = startTime;
        this.startTimeStamp = startTime.getTime() / 1000;
        this.endTime = endTime;
        this.endTimeStamp = endTime.getTime() / 1000;
        this.status = status;
        this.oiContest = oiContest;
        this.link = link;
        updateStatus();
    }

    public void updateStatus() {
        Date now = new Date();
        if (now.after(startTime) && now.before(endTime)) {
            status = ContestStatus.RUNNING;
        } else if (now.after(endTime)) {
            status = ContestStatus.ENDED;
        }
    }

    @Override
    public int compareTo(ContestDto o) {
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

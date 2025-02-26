package com.algo.data.dto;

import com.algo.data.common.ContestStatus;
import com.algo.data.dao.ContestDo;
import lombok.Data;

import java.util.Date;

@Data
public class ContestDto extends BaseDto implements Comparable<ContestDto> {
    private Long id;
    private String oj;
    private String name;
    private Date startTime;
    private Long startTimeStamp;
    private Date endTime;
    private Long endTimeStamp;
    private String status;
    private boolean oiContest;
    private String link;
    private boolean isNotify;

    public ContestDto(String oj, String name, Date startTime, Date endTime, String status, String link) {
        this(oj, name, startTime, endTime, status, false, link, false);
    }

    public ContestDto(String oj, String name, Date startTime, Date endTime, String status, boolean oiContest, String link, boolean isNotify) {
        this.oj = oj;
        this.name = name;
        this.startTime = startTime;
        this.startTimeStamp = startTime.getTime() / 1000;
        this.endTime = endTime;
        this.endTimeStamp = endTime.getTime() / 1000;
        this.status = status;
        this.oiContest = oiContest;
        this.link = link;
        this.isNotify = isNotify;
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

    public static ContestDo toDo(ContestDto dto) {
        ContestDo dobj = new ContestDo();
        dobj.setId(dto.getId());
        dobj.setOj(dto.getOj());
        dobj.setName(dto.getName());
        dobj.setStartTime(dto.getStartTime());
        dobj.setEndTime(dto.getEndTime());
        dobj.setStatus(dto.getStatus());
        dobj.setOiContest(dto.isOiContest());
        dobj.setLink(dto.getLink());
        dobj.setNotify(dto.isNotify());
        return dobj;
    }

    public static ContestDto fromDo(ContestDo dobj) {
        ContestDto dto = new ContestDto(dobj.getOj()
                , dobj.getName()
                , dobj.getStartTime()
                , dobj.getEndTime()
                , dobj.getStatus()
                , dobj.isOiContest()
                , dobj.getLink()
                , dobj.isNotify());
        dto.setId(dobj.getId());
        return dto;
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

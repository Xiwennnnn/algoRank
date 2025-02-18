package com.algo.bot.data;

import com.algo.data.dto.ContestDto;
import com.algo.data.vo.ContestVo;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Data
public class ContestMsg {
    private String oj;
    private String name;
    private String startTime;
    private String duration;
    private String status;
    private String url;

    public static ContestMsg convert(ContestDto dto) {
        ContestMsg QQMsg = new ContestMsg();
        QQMsg.setOj(dto.getOj());
        QQMsg.setName(dto.getName());
        long durationInMillis = dto.getEndTimeStamp() - dto.getStartTimeStamp();
        long durationInMinutes = durationInMillis / 60;
        long hours = durationInMinutes / 60;
        long minutes = durationInMinutes % 60;
        String duration = String.format("%02d:%02d", hours, minutes);
        QQMsg.setDuration(duration);
        Date startDate = dto.getStartTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm", Locale.CHINESE);
        QQMsg.setStartTime(sdf.format(startDate));
        QQMsg.setStatus(dto.getStatus());
        QQMsg.setUrl(dto.getLink());
        return QQMsg;
    }
}

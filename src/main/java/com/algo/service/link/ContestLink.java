package com.algo.service.link;

import cn.hutool.core.date.DateUtil;
import com.algo.data.common.OJEnum;
import com.algo.data.dto.ContestDto;
import com.algo.data.vo.ContestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class ContestLink {
    protected static final String A_LINK_STYLE = " style='cursor: pointer;position: relative;text-decoration: none;color: #000000;' ";
//  解决SDF线程安全问题
    private static final ThreadLocal<SimpleDateFormat> sdfThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy年M月d日 HH:mm", Locale.CHINESE));

    public static ContestVo convert(ContestDto dto) {
        ContestVo vo = new ContestVo();
        vo.setOj(buildOjUrl(dto.getOj()));
        vo.setName(buildContestUrl(dto.getName(), dto.getLink()));
        long durationInMillis = dto.getEndTimeStamp() - dto.getStartTimeStamp();
        long durationInMinutes = durationInMillis / 60;
        long hours = durationInMinutes / 60;
        long minutes = durationInMinutes % 60;
        String duration = String.format("%02d:%02d", hours, minutes);
        vo.setDuration(duration);
        Date startDate = dto.getStartTime();
        SimpleDateFormat sdf = sdfThreadLocal.get();
        vo.setStartTime(sdf.format(startDate));
        vo.setStatus(dto.getStatus());
        Long todayendTime = DateUtil.endOfDay(new Date()).getTime();
        if (dto.getStartTimeStamp() <= todayendTime / 1000) {
            vo.setIsToday(true);
        } else {
            vo.setIsToday(false);
        }
        return vo;
    }

    private static String buildOjUrl(String oj) {
        for (OJEnum ojEnum : OJEnum.values()) {
            if (ojEnum.name().equals(oj)) {
                return "<a style='" + A_LINK_STYLE + "class ='" + ojEnum.name() + "OJ' href='" + ojEnum.url + "' target='_blank'>" + ojEnum.ViewName + "</a>";
            }
        }
        return null;
    }

    private static String buildContestUrl(String name, String url) {
        String res = "<a style='" + A_LINK_STYLE + "class ='" + name +   "CT' href='" + url + "' target='_blank'>" + name + "</a>";
        return res;
    }
}

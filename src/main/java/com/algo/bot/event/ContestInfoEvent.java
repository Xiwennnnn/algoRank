package com.algo.bot.event;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.algo.bot.data.ContestMsg;
import com.algo.bot.data.ErrorMsgEnum;
import com.algo.exception.QQFormatException;
import com.algo.data.common.OJEnum;
import com.algo.data.dto.ContestDto;
import com.algo.service.ContestService;
import com.algo.util.PictureGenerator;
import com.mikuac.shiro.annotation.AnyMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.common.utils.ShiroUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Shiro
@Log
@Component
@EnableScheduling
public class ContestInfoEvent {
    Map<String, OJEnum> ojMap = OJEnum.dialectMap();
    @Resource
    ContestService contestService;

    @Value("${algo.bot.groups}")
    private String qqGroupsStr;

    @Value("${algo.bot.bot-qq}")
    private String botQQ;

    private List<String> qqGroups;
    @Resource
    private BotContainer botContainer;

    private Map<String, Boolean> contestNotifyMap = new HashMap<>();

    private static final ThreadLocal<SimpleDateFormat> sdfThreadLocal = ThreadLocal.withInitial(() ->
            new SimpleDateFormat("yyyy年M月d日", Locale.CHINESE));

    @PostConstruct
    public void init() {
        qqGroups = Arrays.asList(qqGroupsStr.split(","));
    }

    @AnyMessageHandler
    @MessageHandlerFilter(startWith = {"/比赛", "/acm比赛", "/oi比赛", "/ACM比赛", "/OI比赛"})
    public void getRecentContestInfo(Bot bot, AnyMessageEvent event) {
        bot.sendMsg(event, "🚀🚀🚀正在查询比赛信息，请稍后...", false);
        String msg = event.getMessage().toLowerCase();
        if (msg.startsWith("/比赛") || msg.startsWith("/acm比赛")) {
            String args = event.getMessage().replace("/比赛", "").trim();
            args = args.replace("/acm比赛", "").trim();
            Set<String> argsOjs;
            try {
                argsOjs = getOjs(args).stream().map(OJEnum::name).collect(Collectors.toSet());
            } catch (QQFormatException e) {
                bot.sendMsg(event, ErrorMsgEnum.UNKOWN_OJ.msg, false);
                return;
            }
            List<ContestDto> acmContests = contestService.getAcmContests(1);
            List<ContestMsg> acmContestVos = acmContests.stream().filter(contest -> {
                if(!argsOjs.contains(contest.getOj())) {
                    return false;
                }
                return true;
            }).map(ContestMsg::convert).toList();

            List<String> msgList = new ArrayList<>();
            msgList.add("💡近期ACM赛制比赛信息如下");
            for (ContestMsg contestMsg : acmContestVos) {
                msgList.add(buildContestInfoMsg(contestMsg));
            }
            List<Map<String, Object>> forwardMsg = ShiroUtils.generateForwardMsg(msgList);
            bot.sendForwardMsg(event, forwardMsg);
        } else if (msg.startsWith("/oi比赛")) {
            String args = event.getMessage().replace("/oi比赛", "").trim();
            Set<String> argsOjs;
            try {
                argsOjs = getOjs(args).stream().map(OJEnum::name).collect(Collectors.toSet());
            } catch (QQFormatException e) {
                bot.sendMsg(event, ErrorMsgEnum.UNKOWN_OJ.msg, false);
                return;
            }
            List<ContestDto> oiContests = contestService.getOiContests(1);
            List<ContestMsg> oiContestVos = oiContests.stream().filter(contest -> {
                if(!argsOjs.contains(contest.getOj())) {
                    return false;
                }
                return true;
            }).map(ContestMsg::convert).toList();

            List<String> msgList = new ArrayList<>();
            msgList.add("💡近期OI赛制比赛信息如下");
            for (ContestMsg contestMsg : oiContestVos) {
                msgList.add(buildContestInfoMsg(contestMsg));
            }
            List<Map<String, Object>> forwardMsg = ShiroUtils.generateForwardMsg(msgList);
            bot.sendForwardMsg(event, forwardMsg);
        } else {
            bot.sendMsg(event, ErrorMsgEnum.UNDEFINE_CMD.msg, false);
        }
    }

    @Scheduled(fixedDelay = 60 * 1000)
    private void sendRecentContestInfo() {
        long botId = Long.parseLong(botQQ);
        Bot bot = botContainer.robots.get(botId);
        List<ContestDto> acmContests = contestService.getAcmContests(1);
        if (acmContests.isEmpty()) {
            return;
        }
        DateTime oneHourLater = DateUtil.offsetHour(new Date(), 1);
        for (ContestDto contest : acmContests) {
            if (contest.getStartTime().before(oneHourLater.toJdkDate()) && !contest.isNotify()) {
                log.info("比赛即将开始：" + contest.getName());
                String msg = MsgUtils.builder()
                        .text("😍1小时后有一场比赛即将开始啦，请查收：\n")
                        .text(buildContestInfoMsg(ContestMsg.convert(contest)))
                        .build();
                for (String qqGroup : qqGroups) {
                    bot.sendGroupMsg(Long.parseLong(qqGroup), msg, false);
                    contest.setNotify(true);
                    contestService.update(contest);
                }
            }
        }
    }


    @Scheduled(cron = "0 0 8 * * ?")
    public void sendMorningGreetings() {
        long botId = Long.parseLong(botQQ);
        Bot bot = botContainer.robots.get(botId);
        List<ContestDto> acmContests = contestService.getAcmContests(1);
        int TodayCount = 0;
        List<String> msgList = new ArrayList<>();
        for (ContestDto contest : acmContests) {
            if (contest.getStartTime().before(DateUtil.endOfDay(new Date()).toJdkDate())) {
                msgList.add(buildContestInfoMsg(ContestMsg.convert(contest)));
                TodayCount++;
            }
        }
        try {
            byte[] image = PictureGenerator.generateLandscapeImage(1920, 1080);
            String base64 = Base64.getEncoder().encodeToString(image);
            for (String qqGroup : qqGroups) {
                bot.sendGroupMsg(Long.parseLong(qqGroup), MsgUtils.builder().img("base64://" + base64).build(), false);
            }
        } catch (Exception e) {
            log.warning("早晨问好图片发送失败，原因：" + e.getMessage());
        }

        if (TodayCount == 0) {
            for (String qqGroup : qqGroups) {
                DateFormat dateFormat = sdfThreadLocal.get();
                String msg = MsgUtils.builder()
                                .text("⏱️早上好！，今天是" + dateFormat.format(new Date()) + "\n")
                                .text("今天没有比赛，大家要记得坚持刷题哦！💭💡🎈").build();
                bot.sendGroupMsg(Long.parseLong(qqGroup), msg, false);
            }
        } else {
            List<Map<String, Object>> forwardMsg = ShiroUtils.generateForwardMsg(msgList);
            for (String qqGroup : qqGroups) {
                DateFormat dateFormat = sdfThreadLocal.get();
                String msg = MsgUtils.builder()
                                .text("⏱️早上好！，今天是" + dateFormat.format(new Date()) + "\n")
                                .text("今天一共有" + TodayCount + "场比赛，大家要记得参加哦！🎈🎈🎈\n")
                                .text("今比赛信息如下⭐：")
                                .build();
                bot.sendGroupMsg(Long.parseLong(qqGroup), msg, false);
                bot.sendGroupForwardMsg(Long.parseLong(qqGroup), forwardMsg);
            }
        }
    }

    private String buildContestInfoMsg(ContestMsg contest) {
        StringBuilder msg = new StringBuilder("比赛平台：" + contest.getOj() + "\n");
        msg.append("📃比赛名称：" + contest.getName() + "\n");
        msg.append("⏱️比赛开始时间：" + contest.getStartTime() + "\n");
        msg.append("💡比赛持续时间：" + contest.getDuration() + "\n");
        msg.append("🎈比赛链接：" + contest.getUrl());
        return msg.toString();
    }

    private Set<OJEnum> getOjs(String strArgs) throws QQFormatException {
        Set<OJEnum> ojs = new HashSet<>();
        if (strArgs.isEmpty() || strArgs.equals(" ")) {
            ojs.addAll(Arrays.asList(OJEnum.values()));
            return ojs;
        }
        String[] args = strArgs.split(" ");
        for (String arg : args) {
            if (ojMap.containsKey(arg.toLowerCase())) {
                ojs.add(ojMap.get(arg.toLowerCase()));
            } else if (ojMap.containsKey(arg.toUpperCase())) {
                ojs.add(ojMap.get(arg.toUpperCase()));
            } else if (ojMap.containsKey(arg)) {
                ojs.add(ojMap.get(arg));
            } else {
                throw new QQFormatException(ErrorMsgEnum.UNKOWN_OJ.msg);
            }
        }
        return ojs;
    }
}

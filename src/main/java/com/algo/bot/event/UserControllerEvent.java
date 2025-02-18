package com.algo.bot.event;

import com.algo.bot.data.ErrorMsgEnum;
import com.algo.data.common.GradeEnum;
import com.algo.data.dao.AlgoUserDo;
import com.algo.exception.HttpRequestWrongException;
import com.algo.exception.QQFormatException;
import com.algo.crawler.rating.CfRatingCrawler;
import com.algo.crawler.rating.LcRatingCrawler;
import com.algo.data.common.MajorEnum;
import com.algo.data.vo.RatingUserVo;
import com.algo.exception.RatingCrawlerWrongException;
import com.algo.service.RatingService;
import com.mikuac.shiro.annotation.AnyMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Shiro
@Component
@Log
public class UserControllerEvent {
    @Resource
    private LcRatingCrawler lcCrawler;
    @Resource
    private CfRatingCrawler cfCrawler;
    @Resource
    RatingService ratingService;

    @Value("${algo.bot.admin}")
    private String adminStr;

    private List<String> adminUser;

    @PostConstruct
    public void init() {
        adminUser = Arrays.asList(adminStr.split(","));
    }

    @AnyMessageHandler
    @MessageHandlerFilter(startWith = "/用户")
    public void getUserInfo(Bot bot, AnyMessageEvent event) {
        String arg = event.getMessage().replace("/用户", "").trim();
        if (arg.isEmpty()) {
            bot.sendMsg(event, ErrorMsgEnum.EMPTY_ARGS.msg, false);
            return;
        }
        RatingUserVo user = ratingService.getUserVoByRealName(arg);
        if (user == null) {
            bot.sendMsg(event, "🥲没有找到该用户~", false);
            return;
        }
        String msg = MsgUtils.builder()
                .text("💡用户【" + user.getRealName() + "】的信息如下：\n")
                .text("专业：" + user.getMajor() + "\n")
                .text("年级：" + user.getGrade() + "\n")
                .text("力扣：" + user.getLcUsername() + "\n")
                .text("Codeforces：" + user.getCfUsername() + "\n")
                .text("QQ：" + user.getQq())
                .build();
        bot.sendMsg(event, msg, false);
    }

    @AnyMessageHandler
    @MessageHandlerFilter(startWith = "/添加")
    public int addUser(Bot bot, AnyMessageEvent event) {
        String msg = event.getMessage().replace("/添加", "").trim();
        if (msg.isEmpty()) {
            bot.sendMsg(event, ErrorMsgEnum.EMPTY_ARGS.msg, false);
            return 0;
        }
        String[] args = msg.split(" ");
        RatingUserVo user;
        try {
            user = buildUser(args);
        } catch (Exception e) {
            log.warning("添加用户失败，原因：" + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.USER_ADD_FAILED.msg+ e.getMessage(), false);
            return 0;
        }
        if (user.getQq() == null) {
            user.setQq(event.getUserId());
        }
        try {
            ratingService.save(user);
        } catch (Exception e) {
            log.warning("添加用户失败，原因：" + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.USER_ADD_FAILED.msg+ e.getMessage(), false);
            return 0;
        }
        bot.sendMsg(event, "😄用户【" + user.getRealName() + "】添加成功！", false);
        return 1;
    }
    /**
     * 修改用户信息
     * @param bot
     * @param event
     * @return
     */
    @AnyMessageHandler
    @MessageHandlerFilter(startWith = "/修改")
    public int modifyUser(Bot bot, AnyMessageEvent event) {
        String msg = event.getMessage().replace("/修改", "").trim();
        String[] args = msg.split(" ");
        RatingUserVo user;
        try {
            user = collectUserInfo(args);
        } catch (Exception e) {
            log.warning("修改用户信息失败，原因：" + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.USER_MODIFY_FAILED.msg + e.getMessage(), false);
            return 0;
        }
        if (user.getQq() != null && !adminUser.contains((event.getUserId().toString()))) {
            bot.sendMsg(event, ErrorMsgEnum.USER_QQ_CANNOT_MODIFIED.msg, false);
            return 0;
        }
        AlgoUserDo oldUser = ratingService.getByRealName(user.getRealName());
        if (!adminUser.contains(event.getUserId().toString()) && !Objects.equals(oldUser.getQq(), event.getUserId())) {
            bot.sendMsg(event, ErrorMsgEnum.AUTH_NOT_HANDLED.msg, false);
        }
        try {
            ratingService.updateRating(user);
        } catch (Exception e) {
            log.warning("修改用户信息失败，原因：" + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.USER_MODIFY_FAILED.msg + e.getMessage(), false);
            return 0;
        }
        bot.sendMsg(event, "😄用户【" + user.getRealName() + "】信息修改成功！", false);
        return 1;
    }

    @AnyMessageHandler
    @MessageHandlerFilter(startWith = "/删除")
    public int deleteUser(Bot bot, AnyMessageEvent event) {
        String name = event.getMessage().replace("/删除", "").trim();
        if (name.isEmpty()) {
            bot.sendMsg(event, ErrorMsgEnum.USER_DELETE_FAILED.msg + "名字为空~", false);
            return 0;
        }
        AlgoUserDo exitUser = ratingService.getByRealName(name);
        if (!adminUser.contains(event.getUserId().toString()) && !Objects.equals(exitUser.getQq(), event.getUserId())) {
            bot.sendMsg(event, ErrorMsgEnum.AUTH_NOT_HANDLED.msg, false);
            return 0;
        }
        try {
            ratingService.deleteRating(name);
        } catch (Exception e) {
            log.warning("🥲删除用户失败，原因：" + e.getMessage());
            bot.sendGroupMsg(event.getGroupId(), event.getUserId(), ErrorMsgEnum.USER_DELETE_FAILED.msg + "没有找到该用户~", false);
            return 0;
        }
        bot.sendMsg(event, "😄用户【" + name + "】删除成功！", false);
        return 1;
    }


    private RatingUserVo buildUser(String[] args) throws QQFormatException, HttpRequestWrongException, RatingCrawlerWrongException {
        return collectUserInfo(args);
    }

    private RatingUserVo collectUserInfo(String[] args) throws QQFormatException, HttpRequestWrongException, RatingCrawlerWrongException {
        RatingUserVo user = new RatingUserVo();
        user.setRealName(args[0]);
        for (int i = 1; i < args.length; i++) {
            String[] kv = args[i].split("[：:]");
            if (kv.length != 2) {
                throw new QQFormatException("输入格式不正确");
            }
            String key = kv[0], value = kv[1];
            if ("专业".equals(key)) {
                if (!MajorEnum.getAllMajorNames().contains(value)) {
                    throw new QQFormatException("输入的专业名称不正确");
                }
                user.setMajor(value);
            } else if("其他专业".equals(key)) {
                user.setMajor(value);
            } else if ("年级".equals(key)) {
                if (GradeEnum.getAllGradeEnum().contains(value.substring(0, 2))) {
                    user.setGrade(value);
                } else {
                    throw new QQFormatException("输入的年级不正确");
                }
            } else if ("力扣".equals(key) || "leetcode".equalsIgnoreCase(key) || "lc".equalsIgnoreCase(key)) {
                try {
                    lcCrawler.crawl(value);
                } catch (RatingCrawlerWrongException e) {
                    throw new RatingCrawlerWrongException("输入的力扣用户名不正确");
                } catch (HttpRequestWrongException e) {
                    throw new HttpRequestWrongException(ErrorMsgEnum.NETWORK_ERROR.msg);
                }
                user.setLcUsername(value);
            } else if ("cf".equalsIgnoreCase(key) || "codeforces".equalsIgnoreCase(key)) {
                try {
                    cfCrawler.crawl(value);
                } catch (RatingCrawlerWrongException e) {
                    throw new RatingCrawlerWrongException("输入的Codeforces用户名不正确");
                } catch (HttpRequestWrongException e) {
                    throw new HttpRequestWrongException(ErrorMsgEnum.NETWORK_ERROR.msg);
                }
                user.setCfUsername(value);
            } else if ("qq".equals(key)) {
                user.setQq(Long.parseLong(value));
            }
        }
        return user;
    }
}

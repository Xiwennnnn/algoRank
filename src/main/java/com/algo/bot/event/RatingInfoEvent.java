package com.algo.bot.event;

import com.algo.bot.data.ErrorMsgEnum;
import com.algo.bot.data.RealNameMap;
import com.algo.crawler.rating.CfRatingCrawler;
import com.algo.crawler.rating.LcRatingCrawler;
import com.algo.data.dao.AlgoUserDo;
import com.algo.data.dto.codeforces.CfRatingChangeDto;
import com.algo.data.dto.codeforces.CfRatingDto;
import com.algo.data.dto.codeforces.CfStatusDto;
import com.algo.data.dto.leetcode.LanguageAndProblemDto;
import com.algo.data.dto.leetcode.LcRatingDto;
import com.algo.data.dto.leetcode.LcRatingHistoryDto;
import com.algo.exception.HttpRequestWrongException;
import com.algo.exception.RatingCrawlerWrongException;
import com.algo.service.RatingService;
import com.algo.util.PlaywrightUtil;
import com.algo.util.TemplateUtil;
import com.microsoft.playwright.*;
import com.mikuac.shiro.annotation.AnyMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import jakarta.annotation.Resource;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

@Shiro
@Log
@Component
public class RatingInfoEvent {
    @Resource
    private CfRatingCrawler cfCrawler;
    @Resource
    private LcRatingCrawler lcCrawler;
    @Resource
    private RealNameMap realNameMap;
    @Resource
    private RatingService ratingService;

    @AnyMessageHandler
    @MessageHandlerFilter(startWith = "/cf")
    public void getCfProfileCard(Bot bot, AnyMessageEvent event) {
        if (event.getMessage().startsWith("/cfrk")) return;
        String msg = event.getMessage().replace("/cf", "").trim();
        if (msg.isEmpty()) {
            AlgoUserDo user = ratingService.getByQQ(event.getUserId());
            if (user == null) {
                bot.sendMsg(event, ErrorMsgEnum.EMPTY_ARGS.msg, false);
                return;
            }
            msg = user.getRealName();
        }
        String html = "";
        try {
            if (realNameMap.hasCfRealName(msg)) msg = realNameMap.getCfRealName(msg);
            CfRatingDto profile = cfCrawler.crawl(msg);
            List<CfStatusDto> cfStatusDtos = cfCrawler.crawlStatus(msg);
            int cnt = 0;
            Set<String> flags = new HashSet<>();
            for (CfStatusDto cfStatusDto : cfStatusDtos) {
                if (cfStatusDto.getVerdict().equals("OK") && !flags.contains(cfStatusDto.getProblem().getName())) {
                    cnt++;
                    flags.add(cfStatusDto.getProblem().getName());
                }
            }
            html = TemplateUtil.renderCodeforcesProfile(profile, cnt);
        } catch (RatingCrawlerWrongException e) {
            log.warning("获取 Profile 信息失败: " + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.QUERY_FAILED.msg, false);
            return;
        } catch (HttpRequestWrongException e) {
            log.warning("获取 Profile 信息失败: " + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.NETWORK_ERROR.msg, false);
            return;
        }
        try {
            String base64Image = PlaywrightUtil.getScreenshot(html, 400, 225, 2.0);
            bot.sendMsg(event, MsgUtils.builder().img("base64://" + base64Image).build(), false);
        } catch (Exception e) {
            log.warning("图片转换失败：" + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.SERVER_ERROR.msg, false);
        }
    }

    @AnyMessageHandler
    @MessageHandlerFilter(startWith = "/lc")
    public void getLcProfileCard(Bot bot, AnyMessageEvent event) {
        if (event.getMessage().startsWith("/lcrk")) return;

        String msg = event.getMessage().replace("/lc", "").trim();
        if (msg.isEmpty()) {
            AlgoUserDo user = ratingService.getByQQ(event.getUserId());
            if (user == null) {
                bot.sendMsg(event, "请附加你要查询的用户名或绑定自己的用户后再试", false);
                return;
            }
            msg = user.getRealName();
        }

        String html = "";
        try {
            if (realNameMap.hasLcRealName(msg)) msg = realNameMap.getLcRealName(msg);
            LcRatingDto lcDto = lcCrawler.crawl(msg);
            LanguageAndProblemDto languageAndProblemDto = lcCrawler.crawlLanguageAndProblem(msg);
            html = TemplateUtil.renderLeetcodeProfile(lcDto, languageAndProblemDto);
        } catch (RatingCrawlerWrongException e) {
            log.warning("获取 Profile 信息失败: " + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.QUERY_FAILED.msg, false);
            return;
        } catch (HttpRequestWrongException e) {
            log.warning("获取 Profile 信息失败: " + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.NETWORK_ERROR.msg, false);
            return;
        }
        try {
            String base64Image = PlaywrightUtil.getScreenshot(html, 400, 225, 2.0);
            bot.sendMsg(event, MsgUtils.builder().img("base64://" + base64Image).build(), false);
        } catch (Exception e) {
            log.warning("图片转换失败：" + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.SERVER_ERROR.msg, false);
        }
    }


    @AnyMessageHandler
    @MessageHandlerFilter(startWith = "/cfrk")
    public void getCfRatingChangeInfo(Bot bot, AnyMessageEvent event) {
        String msg = event.getMessage().replace("/cfrk", "").trim();
        if (msg.isEmpty()) {
            AlgoUserDo user = ratingService.getByQQ(event.getUserId());
            if (user == null) {
                bot.sendMsg(event, "🥲请附加你要查询的用户名或绑定自己的用户后再试一试~", false);
                return;
            }
            msg = user.getRealName();
        }
        String[] args = msg.split(" ");
        String html;
        try {
            if (args.length == 1) {
                if (realNameMap.hasCfRealName(args[0])) args[0] = realNameMap.getCfRealName(args[0]);
                CfRatingDto profile = cfCrawler.crawl(args[0]);
                List<CfStatusDto> cfStatusDtos = cfCrawler.crawlStatus(args[0]);
                List<CfRatingChangeDto> cfRkList = cfCrawler.crawlRatingChanges(args[0]);
                html = TemplateUtil.renderCodeforcesRankChange(profile, cfRkList, cfStatusDtos);
            } else {
                List<List<CfRatingChangeDto>> cfRkLists = new ArrayList<>();
                for (String arg : args) {
                    if (realNameMap.hasCfRealName(arg)) arg = realNameMap.getCfRealName(arg);
                    cfRkLists.add(cfCrawler.crawlRatingChanges(arg));
                }
                html = TemplateUtil.renderMultipleCodeforcesRankChange(cfRkLists);
            }
        } catch (RatingCrawlerWrongException e) {
            log.warning("获取 Rating 信息失败: " + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.QUERY_FAILED.msg, false);
            return;
        } catch (HttpRequestWrongException e) {
            log.warning("获取 Rating 信息失败: " + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.NETWORK_ERROR.msg, false);
            return;
        }
        try {
            String base64Image = PlaywrightUtil.getScreenshot(html, 1027, 750, 2.0);
            bot.sendMsg(event, MsgUtils.builder().img("base64://" + base64Image).build(), false);
        } catch (Exception e) {
            log.warning("图片转换失败：" + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.SERVER_ERROR.msg, false);
        }
    }


    @AnyMessageHandler
    @MessageHandlerFilter(startWith = "/lcrk")
    public void getLcRatingChangeInfo(Bot bot, AnyMessageEvent event) {
        String msg = event.getMessage().replace("/lcrk", "").trim();
        if (msg.isEmpty()) {
            AlgoUserDo user = ratingService.getByQQ(event.getUserId());
            if (user == null) {
                bot.sendMsg(event, "🥲请附加你要查询的用户名或绑定自己的用户后再试一试~", false);
                return;
            }
            msg = user.getRealName();
        }
        String[] args = msg.split(" ");
        String html = "";
        try {
            if (args.length == 1) {
                if (realNameMap.hasLcRealName(args[0])) args[0] = realNameMap.getLcRealName(args[0]);
                LcRatingHistoryDto lcDto = lcCrawler.crawlRatingChange(args[0]);
                LanguageAndProblemDto languageAndProblemDto = lcCrawler.crawlLanguageAndProblem(args[0]);
                html = TemplateUtil.renderLeetcodeRakingChange(lcDto, languageAndProblemDto);
            } else {
                bot.sendMsg(event, ErrorMsgEnum.NOT_SUPPORTED_MUTI_QUERY.msg, false);
                return;
            }
        } catch (RatingCrawlerWrongException e) {
            log.warning("获取 Rating 信息失败: " + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.QUERY_FAILED.msg, false);
            return;
        } catch (HttpRequestWrongException e) {
            log.warning("获取 Rating 信息失败: " + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.NETWORK_ERROR.msg, false);
            return;
        }
        try {
            String base64Image = PlaywrightUtil.getScreenshot(html, 990, 690, 2.0);
            bot.sendMsg(event, MsgUtils.builder().img("base64://" + base64Image).build(), false);
        } catch (Exception e) {
            log.warning("图片转换失败：" + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.SERVER_ERROR.msg, false);
        }
    }

}

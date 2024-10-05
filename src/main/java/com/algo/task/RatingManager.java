package com.algo.task;

import cn.hutool.core.date.StopWatch;
import com.algo.crawler.rating.CfRatingCrawler;
import com.algo.crawler.rating.LcRatingCrawler;
import com.algo.crawler.rating.RatingBaseCrawler;
import com.algo.crawler.scan.CrawlerScanner;
import com.algo.data.converter.Converter;
import com.algo.data.dao.CfRatingDo;
import com.algo.data.dao.LcRatingDo;
import com.algo.data.dto.CfRatingDto;
import com.algo.data.dto.LcRatingDto;
import com.algo.data.dto.RatingDto;
import com.algo.data.mapper.AlgoUserMapper;
import com.algo.data.mapper.CfRatingMapper;
import com.algo.data.mapper.LcRatingMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@Component
@Slf4j
public class RatingManager {
    // 爬取最大重试次数
    private static final int CRAWL_MAX_RETRY = 3;
    @Resource
    private CrawlerScanner crawlerScanner;
    @Resource
    private LcRatingMapper lcRatingMapper;
    @Resource
    private CfRatingMapper cfRatingMapper;
    @Resource
    private AlgoUserMapper algoUserMapper;

    @Async
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    @CacheEvict(
            cacheNames = {"cfRatingPageCache", "lcRatingPageCache", "cfRatingCache", "lcRatingCache"}
            , allEntries = true
    )
    public void crwlTask() {
        StopWatch stopWatch = new StopWatch("用户评级信息爬虫");
        log.info("【用户评级信息爬虫启动！！！】开始爬取用户评级信息");
        int LcTotal = 0, CfTotal = 0;
        for (RatingBaseCrawler ratingBaseCrawler : crawlerScanner.getRatingCrawlers()) {
            if (ratingBaseCrawler.getClass().getSimpleName().equals("LcRatingCrawler")) {
                stopWatch.start("LeetCode 爬虫");
                LcTotal += crwlLcRating((LcRatingCrawler) ratingBaseCrawler);
                stopWatch.stop();
            } else if (ratingBaseCrawler.getClass().getSimpleName().equals("CfRatingCrawler")) {
                stopWatch.start("Codeforces 爬虫");
                CfTotal += crwlCfRating((CfRatingCrawler) ratingBaseCrawler);
                stopWatch.stop();
            }
        }
        log.info("更新用户评级信息完成，Leetcode 更新数:{}, Codeforces 更新数:{}，共计耗时情况如下：{}", LcTotal, CfTotal, stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    private int crwlLcRating(LcRatingCrawler crawler) {
        int res = 0;
        List<String> usernames = lcRatingMapper.selectList(Wrappers.emptyWrapper()).stream().map(LcRatingDo::getUserName).toList();
        for (String username : usernames) {
            LcRatingDo lcRatingDO = null;
            for (int i = 0; i < CRAWL_MAX_RETRY; i++) {
                try {
                    LcRatingDto ratingDTO = (LcRatingDto) crawler.crawl(username);
                    lcRatingDO = Converter.convertLcRatingDTOToDO(ratingDTO);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            UpdateWrapper<LcRatingDo> updateWrapper = new UpdateWrapper<>();

            res += lcRatingMapper.update(lcRatingDO, updateWrapper.eq("user_name", lcRatingDO.getUserName()));
        }
        return res;
    }

    private int crwlCfRating(CfRatingCrawler crawler) {
        int res = 0;
        List<String> usernames = cfRatingMapper.selectList(Wrappers.emptyWrapper()).stream().map(CfRatingDo::getUserName).toList();
        List<RatingDto> result = null;
        for (int i = 0; i < CRAWL_MAX_RETRY; i++) {
            try {
                result = crawler.crawlALl(usernames);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (RatingDto ratingDTO : result) {
            CfRatingDo cfRatingDO = Converter.convertCfRatingDTOToDO((CfRatingDto) ratingDTO);
            UpdateWrapper<CfRatingDo> updateWrapper = new UpdateWrapper<>();
            res += cfRatingMapper.update(cfRatingDO, updateWrapper.eq("user_name", cfRatingDO.getUserName()));
        }
        return res;
    }
}

package com.algo.task;

import cn.hutool.core.date.StopWatch;
import com.algo.crawler.contest.ContestBaseCrawler;
import com.algo.crawler.scan.CrawlerScanner;
import com.algo.data.dto.ContestDto;
import com.algo.service.ContestService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@EnableScheduling
@Component
@Slf4j
public class ContestManager {
    // 爬取最大重试次数
    private static final int CRAWL_MAX_RETRY = 3;
    private static final int SUM_LIMIT = 100;
    @Resource
    private CrawlerScanner crawlerScanner;
    @Resource
    private ContestService contestService;
    private Map<String, Set<ContestDto>> contestMap = new ConcurrentHashMap<>();

    public List<ContestDto> getContests() {
        return query(x -> true);
    }

    private List<ContestDto> query(Predicate<? super ContestDto> cond) {
        List<ContestDto> contestList = new ArrayList<>();

        for (Set<ContestDto> set : contestMap.values()) {
            contestList.addAll(queryFromOJ(set, cond));
        }

        return contestList.stream()
                .sorted()
                .limit(SUM_LIMIT)
                .collect(Collectors.toList());
    }

    private List<ContestDto> queryFromOJ(Set<ContestDto> originSet, Predicate<? super ContestDto> cond) {
        Date now = new Date();
        return originSet.stream()
                .sorted()
                .filter(x -> x.getEndTime().after(now))
                .filter(cond)
//                .limit(OJ_LIMIT)
                .collect(Collectors.toList());
    }

    @Async
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void crawlTask() {
        StopWatch stopWatch = new StopWatch("ContestManager");

        log.info("【竞赛爬虫启动！！！】开始爬取比赛信息");
        Map<String, Set<ContestDto>> newMap = new HashMap<>();

        for (ContestBaseCrawler crawler : crawlerScanner.getContestCrawlers()) {
            String name = crawler.getClass().getSimpleName().replace("Crawler", "");
            stopWatch.start(name);
            for (int i = 0; i < CRAWL_MAX_RETRY; i++) {
                try {
                    // 爬取一次
                    crawlOne(crawler);
                    Set<ContestDto> set = new TreeSet<>(crawlOne(crawler));
                    newMap.put(name, set);
                    log.info("【{}】平台爬取成功，共获取到{}个比赛", name, set.size());
                    break;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            stopWatch.stop();
        }

        contestMap = newMap;
        updateContest();
        log.info("比赛信息更新完成，如下为信息获取耗时：\n" + stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    @Async
    @Scheduled(fixedDelay = 60 * 1000)
    public void updateStatusTask() {
        contestService.updateStatus();
    }

    private void updateContest() {
        List<ContestDto> allContests = getContests();
        contestService.saveOrUpdateContest(allContests);
    }

    private List<ContestDto> crawlOne(ContestBaseCrawler crawler) throws Exception {
        try {
            return crawler.crawl();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

package com.algo.crawler.scan;

import com.algo.crawler.BaseCrawler;
import com.algo.crawler.contest.ContestBaseCrawler;
import com.algo.crawler.rating.RatingBaseCrawler;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@Slf4j
public class CrawlerScanner implements ApplicationContextAware {
    private ApplicationContext context;

    private ContestBaseCrawler[] contestCrawlers;
    private RatingBaseCrawler[] ratingCrawlers;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @PostConstruct
    public void Contestscan() {
        log.info("【CrawlerScanner】：开始扫描ContestBaseCrawler和RatingBaseCrawler");
        Map<String, ContestBaseCrawler> contestCrawlerMap = context.getBeansOfType(ContestBaseCrawler.class);
        Map<String, RatingBaseCrawler> ratingCrawlerMap = context.getBeansOfType(RatingBaseCrawler.class);

        ContestBaseCrawler[] ccrawlers = new ContestBaseCrawler[contestCrawlerMap.size()];
        contestCrawlerMap.values().toArray(ccrawlers);
        this.contestCrawlers = ccrawlers;

        RatingBaseCrawler[] rcrawlers = new RatingBaseCrawler[ratingCrawlerMap.size()];
        ratingCrawlerMap.values().toArray(rcrawlers);
        log.info("【CrawlerScanner】：扫描ContestBaseCrawler和RatingBaseCrawler完成，【竞赛爬虫数量】：" + ccrawlers.length + "，【评级爬虫数量】：" + rcrawlers.length);
        this.ratingCrawlers = rcrawlers;

    }

}

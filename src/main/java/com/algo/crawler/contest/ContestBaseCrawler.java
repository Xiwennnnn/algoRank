package com.algo.crawler.contest;

import com.algo.crawler.BaseCrawler;
import com.algo.data.dto.ContestDto;
import com.algo.exception.ContestCrawlerWrtongException;

import java.util.ArrayList;
import java.util.List;

public class ContestBaseCrawler extends BaseCrawler {
    public List<ContestDto> crawl() throws ContestCrawlerWrtongException {
        return new ArrayList<>();
    }
}

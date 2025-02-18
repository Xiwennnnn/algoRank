package com.algo.crawler.rating;

import com.algo.crawler.BaseCrawler;
import com.algo.data.dto.RatingDto;
import com.algo.exception.HttpRequestWrongException;
import com.algo.exception.RatingCrawlerWrongException;

import java.io.IOException;

public class RatingBaseCrawler extends BaseCrawler {
    public RatingDto crawl(String username) throws RatingCrawlerWrongException, HttpRequestWrongException {return null;}
}

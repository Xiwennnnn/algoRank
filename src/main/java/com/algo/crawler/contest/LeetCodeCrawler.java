package com.algo.crawler.contest;

import com.algo.data.common.ContestStatus;
import com.algo.data.dto.ContestDto;
import com.algo.exception.ContestCrawlerWrtongException;
import com.algo.exception.HttpRequestWrongException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class LeetCodeCrawler extends ContestBaseCrawler {
    private static final String URL = "https://leetcode.cn/graphql";
    private static final String REQUEST_BODY = """
            {
            	"operationName": null,
            	"variables": {},
            	"query": "{\\n  brightTitle\\n  contestUpcomingContests {\\n    containsPremium\\n    title\\n    cardImg\\n    titleSlug\\n    description\\n    startTime\\n    duration\\n    originStartTime\\n    isVirtual\\n    company {\\n      watermark\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n"
            }
            """;

    private final Map<String, String> headers = new HashMap<>();

    public LeetCodeCrawler() {
        headers.put("Content-Type", "application/json");
    }

    @Override
    public List<ContestDto> crawl() throws ContestCrawlerWrtongException {
        JsonNode contests = null;
        List<ContestDto> contestList = null;
        try {
            contestList = new ArrayList<>();
            contests = getContestsJson(post(URL, REQUEST_BODY, headers));
        } catch (HttpRequestWrongException ex) {
            throw new ContestCrawlerWrtongException("获取Leetcode比赛信息失败" + ex.getMessage());
        }
        Date now = new Date();
        for (JsonNode contest : contests) {
            ContestDto ContestInfo = parseContest(contest);
            // 竞赛个数太多, 提前过滤
            if (ContestInfo.getEndTime().after(now)) {
                contestList.add(parseContest(contest));
            }
        }

        return contestList;
    }

    private JsonNode getContestsJson(String data) throws ContestCrawlerWrtongException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(data).get("data").get("contestUpcomingContests");
        } catch (IOException ex) {
            throw new ContestCrawlerWrtongException("解析Leetcode比赛信息失败Json数据异常");
        }
    }

    private ContestDto parseContest(JsonNode contest) {
        String nameSulg = contest.get("titleSlug").asText();
        String name = contest.get("title").asText();

        long duration = contest.get("duration").asLong() * 1000;
        Date startTime = new Date(contest.get("startTime").asLong() * 1000);
        Date endTime = new Date(startTime.getTime() + duration);

        String status = ContestStatus.REGISTER;
        String link = "https://leetcode.cn/contest/" + nameSulg;

        return new ContestDto("LeetCode", name, startTime, endTime, status, false, link, false);
    }
}

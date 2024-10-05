package com.algo.crawler.contest;

import com.algo.data.common.ContestStatus;
import com.algo.data.dto.ContestDto;
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
    public List<ContestDto> crawl() throws IOException {
        Document doc = Jsoup.parse(post(URL, REQUEST_BODY, headers));
        List<ContestDto> contestList = new ArrayList<>();
        // 请求数据
        JsonNode contests = null;
        try {
            contests = getContestsJson(post(URL, REQUEST_BODY, headers));
        } catch (IOException ex) {
            throw new RuntimeException("Failed to get LeetCode contests", ex);
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

    private JsonNode getContestsJson(String data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(data).get("data").get("contestUpcomingContests");
        } catch (IOException ex) {
            ex.printStackTrace();
            return JsonNodeFactory.instance.arrayNode();
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

        return new ContestDto("LeetCode", name, startTime, endTime, status, false, link);
    }
}

package com.algo.crawler.contest;

import com.algo.data.common.ContestStatus;
import com.algo.data.dto.ContestDto;
import com.algo.exception.ContestCrawlerWrtongException;
import com.algo.exception.HttpRequestWrongException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class LuoGuCrawler extends ContestBaseCrawler {
    private static final String URL = "https://www.luogu.org/contest/list?page=1&_contentOnly=1";

    @Override
    public List<ContestDto> crawl() throws ContestCrawlerWrtongException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode data = objectMapper.readTree(get(URL)).get("currentData").get("contests").get("result");
            return parseContests(data);
        } catch (HttpRequestWrongException ex) {
            throw new ContestCrawlerWrtongException(ex.getMessage());
        } catch (IOException ex) {
            throw new ContestCrawlerWrtongException("LuoGu比赛Json数据解析失败");
        }
    }

    private List<ContestDto> parseContests(JsonNode data) {
        List<ContestDto> contestList = new ArrayList<>();
        for (JsonNode item : data) {
            contestList.add(parseContest(item));
        }

        return contestList;
    }

    private ContestDto parseContest(JsonNode item) {
        String name = item.get("name").asText();
        Date startTime = new Date(item.get("startTime").asLong() * 1000);
        Date endTime = new Date(item.get("endTime").asLong() * 1000);
        String status = ContestStatus.REGISTER;
        boolean oiContest = item.get("ruleType").asInt() != 2;
        String link = "https://www.luogu.org/contest/" + item.get("id").asText();

        return new ContestDto("LuoGu", name, startTime, endTime, status, oiContest, link, false);
    }
}

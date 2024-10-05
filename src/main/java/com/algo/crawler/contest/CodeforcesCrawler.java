package com.algo.crawler.contest;
import com.algo.data.common.ContestStatus;
import com.algo.data.dto.ContestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class CodeforcesCrawler extends ContestBaseCrawler {
    private static final String URL = "https://codeforces.com/api/contest.list?gym=false";

    @Override
    public List<ContestDto> crawl() throws IOException {
        List<ContestDto> contestList = new ArrayList<>();
        JsonNode jsonNode = null;
        try {
            jsonNode = parseContestJson(get(URL));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to crawl Codeforces contests");
        }
        //遍历jsonNode
        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()) {
            ContestDto contestInfo = parseContest(elements.next());
            //如果比赛已经结束了，则不添加到列表中，并break
            if (contestInfo.getEndTime().before(new Date())) {
                break;
            }
            contestList.add(contestInfo);
        }
        contestList.sort(Comparator.comparing(ContestDto::getStartTime));
        return contestList;
    }

    private JsonNode parseContestJson(String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(jsonStr).get("result");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonNodeFactory.instance.arrayNode();
        }
    }

    private ContestDto parseContest(JsonNode contest) {

        String id = contest.get("id").asText();
        String link = "https://codeforces.com/contest/" + id;
        String name = contest.get("name").asText();
        Date startTime = new Date(contest.get("startTimeSeconds").asLong() * 1000);
        Date endTime = new Date(contest.get("durationSeconds").asLong() * 1000 + startTime.getTime());


        boolean register = contest.get("phase").asText().equals("BEFORE");

        String status = ContestStatus.PUBLIC;
        if (register) {
            status = ContestStatus.REGISTER;
        }

        return new ContestDto("Codeforces", name, startTime, endTime, status, link);
    }

    private Date convertEndTime(Date startTime, String lengthString) {
        String pattern = lengthString.length() <= 5 ? "HH:mm" : "dd:HH:mm";
        Date length = parseDate(lengthString, pattern, "UTC");

        return new Date(startTime.getTime() + length.getTime());
    }
}

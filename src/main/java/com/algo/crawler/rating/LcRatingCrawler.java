package com.algo.crawler.rating;

import com.algo.data.dto.LcRatingDto;
import com.algo.data.dto.RatingDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class LcRatingCrawler extends RatingBaseCrawler{
    private static final String URL = "https://leetcode.cn/graphql/noj-go/";
    private static final String RATING_REQUEST_BODY = """
            {
                "query": "\\n    query userContestRankingInfo($userSlug: String!) {\\n  userContestRanking(userSlug: $userSlug) {\\n    attendedContestsCount\\n    rating\\n    globalRanking\\n    localRanking\\n    globalTotalParticipants\\n    localTotalParticipants\\n    topPercentage\\n  }\\n}\\n    ",
                "variables": {
                    "userSlug": "<userName>"
                },
                "operationName": "userContestRankingInfo"
            }
        """;

    private static final String GRADING_REQUEST_BODY = """
            {
                "query": "\\n    query contestBadge($userSlug: String!) {\\n  userProfileUserLevelMedal(userSlug: $userSlug) {\\n    current {\\n      name\\n    }\\n  }\\n}\\n    ",
                "variables": {
                    "userSlug": "<userName>"
                },
                "operationName": "contestBadge"
            }
        """;


    private final Map<String, String> headers = new HashMap<>();

    public LcRatingCrawler() {
        headers.put("Content-Type", "application/json");
    }

    @Override
    public RatingDto crawl(String username) throws IOException {
        String getRatingBody = RATING_REQUEST_BODY.replace("<userName>", username);
        String getGradingBody = GRADING_REQUEST_BODY.replace("<userName>", username);
        JsonNode ratingJson = null;
        JsonNode gradingJson = null;
        try {
            ratingJson = parseRatingJson(post(URL, getRatingBody, headers));
            gradingJson = parseGradingJson(post(URL, getGradingBody, headers));
        } catch (IOException e) {
            throw new IOException("Failed to crawl LeetCode rating", e);
        }
        LcRatingDto ratingInfo = parseRating(ratingJson, gradingJson);
        ratingInfo.setUserName(username);
        return ratingInfo;
    }

    private JsonNode parseRatingJson(String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(data).get("data").get("userContestRanking");
        } catch (IOException e) {
            e.printStackTrace();
            // 返回空JsonNode
            return JsonNodeFactory.instance.arrayNode();
        }
    }

    private JsonNode parseGradingJson(String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(data).get("data").get("userProfileUserLevelMedal");
        } catch (IOException e) {
            e.printStackTrace();
            // 返回空JsonNode
            return JsonNodeFactory.instance.arrayNode();
        }
    }

    private LcRatingDto parseRating(JsonNode ratingJson, JsonNode gradingJson) {
        LcRatingDto ratingInfo = new LcRatingDto();

        // 检查字段是否存在，若不存在则赋予默认值
        ratingInfo.setRating(ratingJson.has("rating") ? ratingJson.get("rating").asInt() : 0);
        ratingInfo.setNationRank(ratingJson.has("localRanking") ? ratingJson.get("localRanking").asInt() : 0);
        ratingInfo.setTopPercentage(ratingJson.has("topPercentage") ? ratingJson.get("topPercentage").asDouble() : 100.0);

        // 处理 gradingJson 中的字段
        if (gradingJson.has("current") && gradingJson.get("current").has("name")) {
            ratingInfo.setGrading(gradingJson.get("current").get("name").asText());
        } else {
            ratingInfo.setGrading("null"); // 默认值为NULL
        }

        return ratingInfo;
    }

}

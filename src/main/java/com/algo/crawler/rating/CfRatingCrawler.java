package com.algo.crawler.rating;

import com.algo.data.dto.CfRatingDto;
import com.algo.data.dto.RatingDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CfRatingCrawler extends RatingBaseCrawler {
    private static final String BASE_URL = "https://codeforces.com/api/user.info?";

    @Override
    public RatingDto crawl(String username) {
        String url = BASE_URL + "handles=" + username;
        JsonNode jsonNode = null;
        try {
            jsonNode = parseRatingJson(get(url));
        } catch (IOException e) {
            throw new RuntimeException("Codeforces API is not available.");
        }
        CfRatingDto rating = parseRating(jsonNode.get(0));
        return rating;
    }

    public List<RatingDto> crawlALl(List<String> usernames) throws IOException {
        String url = BASE_URL + "handles=" + String.join(";", usernames);
        JsonNode jsonNode = null;
        try {
            jsonNode = parseRatingJson(get(url));
        } catch (IOException e) {
            throw new RuntimeException("Codeforces API is not available.");
        }
        List<RatingDto> ratings = new ArrayList<>();
        jsonNode.elements().forEachRemaining(node -> {
            ratings.add(parseRating(node));
        });
        return ratings;
    }

    private JsonNode parseRatingJson(String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(data).get("result");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonNodeFactory.instance.arrayNode();
        }
    }

    private CfRatingDto parseRating(JsonNode jsonNode) {
        CfRatingDto rating = new CfRatingDto();

        // 检查字段是否存在，如果不存在则赋予默认值
        rating.setRating(jsonNode.has("rating") ? jsonNode.get("rating").asInt() : 0);
        rating.setUserName(jsonNode.has("handle") ? jsonNode.get("handle").asText() : "null");
        rating.setRank(jsonNode.has("rank") ? jsonNode.get("rank").asText() : "null");
        rating.setMaxRating(jsonNode.has("maxRating") ? jsonNode.get("maxRating").asInt() : 0);

        return rating;
    }


}

package com.algo.crawler.rating;

import com.algo.data.dto.codeforces.*;
import com.algo.data.dto.RatingDto;
import com.algo.exception.HttpRequestWrongException;
import com.algo.exception.RatingCrawlerWrongException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CfRatingCrawler extends RatingBaseCrawler {
    private static final String USER_INFO__URL = "https://codeforces.com/api/user.info?";
    private static final String USER_STATUS__URL = "https://codeforces.com/api/user.status?";

    @Override
    public CfRatingDto crawl(String username) throws RatingCrawlerWrongException, HttpRequestWrongException {
        String url = USER_INFO__URL + "handles=" + username;
        JsonNode jsonNode;
        try {
            jsonNode = parseJson(get(url));
        } catch (HttpRequestWrongException e) {
            throw new HttpRequestWrongException(e.getMessage());
        }
        CfRatingDto rating = parseRating(jsonNode.get(0));
        return rating;
    }

    public List<RatingDto> crawlALl(List<String> usernames) throws RatingCrawlerWrongException, HttpRequestWrongException {
        String url = USER_INFO__URL + "handles=" + String.join(";", usernames);
        JsonNode jsonNode;
        try {
            jsonNode = parseJson(get(url));
        } catch (HttpRequestWrongException e) {
            throw new HttpRequestWrongException(e.getMessage());
        }
        List<RatingDto> ratings = new ArrayList<>();
        jsonNode.elements().forEachRemaining(node -> {
            ratings.add(parseRating(node));
        });
        return ratings;
    }

    public List<CfRatingChangeDto> crawlRatingChanges(String username) throws RatingCrawlerWrongException, HttpRequestWrongException {
        String url = "https://codeforces.com/api/user.rating?handle=" + username;
        JsonNode jsonNode;
        try {
            jsonNode = parseJson(get(url));
        } catch (HttpRequestWrongException e) {
            throw new HttpRequestWrongException(e.getMessage());
        }
        List<CfRatingChangeDto> ratingChanges = new ArrayList<>();
        jsonNode.elements().forEachRemaining(node -> {
            ratingChanges.add(parseRatingChange(node));
        });
        return ratingChanges;
    }

    public List<CfStatusDto> crawlStatus(String username) throws RatingCrawlerWrongException, HttpRequestWrongException {
        String url = USER_STATUS__URL + "handle=" + username;
        JsonNode jsonNode;
        try {
            jsonNode = parseJson(get(url));
        } catch (HttpRequestWrongException e) {
            throw new HttpRequestWrongException(e.getMessage());
        }
        List<CfStatusDto> statuses = new ArrayList<>();
        jsonNode.elements().forEachRemaining(node -> {
            statuses.add(parseStatus(node));
        });
        return statuses;
    }

    private JsonNode parseJson(String data) throws RatingCrawlerWrongException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(data).get("result");
        } catch (Exception e) {
            throw new RatingCrawlerWrongException("Codeforces API返回的 Json 格式转化错误");
        }
    }

    private CfStatusDto parseStatus(JsonNode jsonNode) {
        CfStatusDto status = new CfStatusDto();
        status.setId(jsonNode.has("id") ? jsonNode.get("id").asLong() : 0);
        status.setContestId(jsonNode.has("contestId") ? jsonNode.get("contestId").asLong() : 0);
        status.setCreationTimeSeconds(jsonNode.has("creationTimeSeconds") ? jsonNode.get("creationTimeSeconds").asLong() : 0);
        status.setProblem(jsonNode.has("problem") ? parseProblem(jsonNode.get("problem")) : null);
        status.setAuthor(jsonNode.has("author") ? parseAuthor(jsonNode.get("author")) : null);
        status.setProgrammingLanguage(jsonNode.has("programmingLanguage") ? jsonNode.get("programmingLanguage").asText() : "null");
        status.setVerdict(jsonNode.has("verdict") ? jsonNode.get("verdict").asText() : "null");
        status.setTestset(jsonNode.has("testset") ? jsonNode.get("testset").asText() : "null");
        status.setPassedTestCount(jsonNode.has("passedTestCount") ? jsonNode.get("passedTestCount").asInt() : 0);
        status.setTimeConsumedMillis(jsonNode.has("timeConsumedMillis") ? jsonNode.get("timeConsumedMillis").asInt() : 0);
        status.setMemoryConsumedBytes(jsonNode.has("memoryConsumedBytes") ? jsonNode.get("memoryConsumedBytes").asLong() : 0);
        return status;
    }

    private CfProblemDto parseProblem(JsonNode jsonNode) {
        CfProblemDto problem = new CfProblemDto();
        problem.setContestId(jsonNode.has("contestId") ? jsonNode.get("contestId").asLong() : 0);
        problem.setName(jsonNode.has("name") ? jsonNode.get("name").asText() : "null");
        problem.setIndex(jsonNode.has("index") ? jsonNode.get("index").asText() : "null");
        problem.setType(jsonNode.has("type") ? jsonNode.get("type").asText() : "null");
        problem.setRating(jsonNode.has("rating") ? jsonNode.get("rating").asInt() : 0);
        List<String> tags = new ArrayList<>();
        if (jsonNode.has("tags")) {
            jsonNode.get("tags").elements().forEachRemaining(tag -> {
                tags.add(tag.asText());
            });
        }
        problem.setTags(tags);
        return problem;
    }

    private CfAuthorDto parseAuthor(JsonNode jsonNode) {
        CfAuthorDto author = new CfAuthorDto();
        author.setContestId(jsonNode.has("contestId") ? jsonNode.get("contestId").asLong() : 0);
        List<String> members = new ArrayList<>();
        if (jsonNode.has("members")) {
            jsonNode.get("members").elements().forEachRemaining(member -> {
                members.add(member.asText());
            });
        }
        author.setMembers(members);
        author.setParticipantType(jsonNode.has("participantType") ? jsonNode.get("participantType").asText() : "null");
        author.setGhost(jsonNode.has("ghost") && jsonNode.get("ghost").asBoolean());
        author.setStartTimeSeconds(jsonNode.has("startTimeSeconds") ? jsonNode.get("startTimeSeconds").asLong() : 0);
        return author;
    }

    private CfRatingChangeDto parseRatingChange(JsonNode jsonNode) {
        CfRatingChangeDto ratingChange = new CfRatingChangeDto();
        ratingChange.setContestId(jsonNode.has("contestId") ? jsonNode.get("contestId").asInt() : 0);
        ratingChange.setContestName(jsonNode.has("contestName") ? jsonNode.get("contestName").asText() : "null");
        ratingChange.setHandle(jsonNode.has("handle") ? jsonNode.get("handle").asText() : "null");
        ratingChange.setRank(jsonNode.has("rank") ? jsonNode.get("rank").asInt() : 0);
        ratingChange.setRatingUpdateTimeSeconds(jsonNode.has("ratingUpdateTimeSeconds") ? jsonNode.get("ratingUpdateTimeSeconds").asLong() : 0);
        ratingChange.setOldRating(jsonNode.has("oldRating") ? jsonNode.get("oldRating").asInt() : 0);
        ratingChange.setNewRating(jsonNode.has("newRating") ? jsonNode.get("newRating").asInt() : 0);
        return ratingChange;
    }

    private CfRatingDto parseRating(JsonNode jsonNode) {
        CfRatingDto rating = new CfRatingDto();
        rating.setRating(jsonNode.has("rating") ? jsonNode.get("rating").asInt() : 0);
        rating.setUserName(jsonNode.has("handle") ? jsonNode.get("handle").asText() : "null");
        rating.setRank(jsonNode.has("rank") ? jsonNode.get("rank").asText() : "null");
        rating.setMaxRating(jsonNode.has("maxRating") ? jsonNode.get("maxRating").asInt() : 0);
        rating.setAvatar(jsonNode.has("avatar") ? jsonNode.get("avatar").asText() : "null");
        rating.setRegistrationTimeSeconds(jsonNode.has("registrationTimeSeconds") ? jsonNode.get("registrationTimeSeconds").asLong() : 0);

        return rating;
    }


}

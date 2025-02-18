package com.algo.crawler.rating;

import com.algo.data.dto.leetcode.LanguageAndProblemDto;
import com.algo.data.dto.leetcode.LcRatingChangeDto;
import com.algo.data.dto.leetcode.LcRatingDto;
import com.algo.data.dto.leetcode.LcRatingHistoryDto;
import com.algo.data.dto.RatingDto;
import com.algo.exception.HttpRequestWrongException;
import com.algo.exception.RatingCrawlerWrongException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class LcRatingCrawler extends RatingBaseCrawler{
    private static final String NOJ_GO_URL = "https://leetcode.cn/graphql/noj-go/";
    private static final String GRAPHQL_URL = "https://leetcode.cn/graphql/";
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

    private static final String CONTEST_RANK_CHANGE_REQUEST_BODY = """
            {
                "query": "\\n    query userContestRankingInfo($userSlug: String!) {\\n  userContestRanking(userSlug: $userSlug) {\\n    attendedContestsCount\\n    rating\\n    globalRanking\\n    localRanking\\n    globalTotalParticipants\\n    localTotalParticipants\\n    topPercentage\\n  }\\n  userContestRankingHistory(userSlug: $userSlug) {\\n    attended\\n    totalProblems\\n    trendingDirection\\n    finishTimeInSeconds\\n    rating\\n    score\\n    ranking\\n    contest {\\n      title\\n      titleCn\\n      startTime\\n    }\\n  }\\n}\\n    ",
                "variables": {
                    "userSlug": "<userName>"
                },
                "operationName": "userContestRankingInfo"
            }
    """;

    private static final String PROFILE_REQUEST_BODY = """
            {
                "query": "\\n    query userProfilePublicProfile($userSlug: String!) {\\n  userProfilePublicProfile(userSlug: $userSlug) {\\n    profile {\\n      userSlug\\n      realName\\n      userAvatar\\n    }\\n  }\\n}\\n    ",
                "variables": {
                    "userSlug": "<userName>"
                },
                "operationName": "userProfilePublicProfile"
            }
    """;

    private static final String PROBLEM_SOLVED_REQUEST_BODY = """
            {
                "query": "\\n    query userSessionProgress($userSlug: String!) {\\n  userProfileUserQuestionProgress(userSlug: $userSlug) {\\n    numAcceptedQuestions {\\n      difficulty\\n      count\\n    }\\n  }\\n}\\n    ",
                "variables": {
                    "userSlug": "<userName>"
                },
                "operationName": "userSessionProgress"
            }
    """;

    private static final String LANGUAGE_USED_REQUEST_BODY = """
            {
                "query": "\\n    query languageStats($userSlug: String!) {\\n  userLanguageProblemCount(userSlug: $userSlug) {\\n    languageName\\n    problemsSolved\\n  }\\n}\\n    ",
                "variables": {
                    "userSlug": "<userName>"
                },
                "operationName": "languageStats"
            }
    """;

    private final Map<String, String> headers = new HashMap<>();

    public LcRatingCrawler() {
        headers.put("Content-Type", "application/json");
    }

    @Override
    public LcRatingDto crawl(String username) throws RatingCrawlerWrongException, HttpRequestWrongException {
        String getRatingBody = RATING_REQUEST_BODY.replace("<userName>", username);
        String getGradingBody = GRADING_REQUEST_BODY.replace("<userName>", username);
        String getProfileBody = PROFILE_REQUEST_BODY.replace("<userName>", username);
        JsonNode ratingJson, gradingJson, profileJson;
        try {
            ratingJson = parseRatingJson(post(NOJ_GO_URL, getRatingBody, headers));
            gradingJson = parseGradingJson(post(NOJ_GO_URL, getGradingBody, headers));
            profileJson = parseProfileJson(post(GRAPHQL_URL, getProfileBody, headers));
        } catch (HttpRequestWrongException e) {
            throw new HttpRequestWrongException(e.getMessage());
        }
        LcRatingDto ratingInfo = parseRating(ratingJson, gradingJson);
        ratingInfo.setUserName(username);
        ratingInfo.setRealName(profileJson.get("realName").asText());
        ratingInfo.setAvatarUrl(profileJson.get("userAvatar").asText());
        return ratingInfo;
    }

    public LcRatingHistoryDto crawlRatingChange(String username) throws RatingCrawlerWrongException, HttpRequestWrongException {
        String getRatingChangeBody = CONTEST_RANK_CHANGE_REQUEST_BODY.replace("<userName>", username);
        String response;
        try {
             response = post(NOJ_GO_URL, getRatingChangeBody, headers);
        } catch (HttpRequestWrongException e) {
            throw new HttpRequestWrongException(e.getMessage());
        }
        JsonNode ratingChangeJson = parseRatingChangeJson(response);
        List<LcRatingChangeDto> ratingChangeList = new ArrayList<>();
        ratingChangeJson.elements().forEachRemaining(node -> {
            LcRatingChangeDto ratingChange = parseRatingChange(node);
            ratingChange.setHandle(username);
            if (ratingChange.isAttended()) ratingChangeList.add(ratingChange);
        });
        LcRatingHistoryDto ratingHistory = new LcRatingHistoryDto();
        ratingHistory.setRatingChanges(ratingChangeList);
        ratingHistory.setLcRatingDto(crawl(username));
        return ratingHistory;
    }

    public LanguageAndProblemDto crawlLanguageAndProblem(String username) throws RatingCrawlerWrongException, HttpRequestWrongException {
        String getLanguageUsedBody = LANGUAGE_USED_REQUEST_BODY.replace("<userName>", username);
        String getProblemSolvedBody = PROBLEM_SOLVED_REQUEST_BODY.replace("<userName>", username);
        LanguageAndProblemDto languageAndProblemDto = new LanguageAndProblemDto();
        try {
            JsonNode languageUsedJson = parseLanguageUsedJson(post(NOJ_GO_URL, getLanguageUsedBody, headers));
            languageUsedJson.elements().forEachRemaining(node -> {
                languageAndProblemDto.getLanguageMap().put(node.get("languageName").asText(), node.get("problemsSolved").asInt());
            });
            JsonNode problemSolvedJson = parseProblemSolvedJson(post(GRAPHQL_URL, getProblemSolvedBody, headers));
            problemSolvedJson.elements().forEachRemaining(node -> {
                if (node.get("difficulty").asText().equals("HARD"))
                    languageAndProblemDto.setHARD(node.get("count").asInt());
                else if (node.get("difficulty").asText().equals("MEDIUM"))
                    languageAndProblemDto.setMEDIUM(node.get("count").asInt());
                else if (node.get("difficulty").asText().equals("EASY"))
                    languageAndProblemDto.setEASY(node.get("count").asInt());
            });
        } catch (HttpRequestWrongException e) {
            throw new HttpRequestWrongException(e.getMessage());
        }
        return languageAndProblemDto;
    }

    private JsonNode parseProblemSolvedJson(String data) throws RatingCrawlerWrongException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode res;
        try {
            res = mapper.readTree(data).get("data").get("userProfileUserQuestionProgress").get("numAcceptedQuestions");
        } catch (Exception e) {
            throw new RatingCrawlerWrongException("解析题目解决信息失败");
        }
        if (res == null) {
            throw new RatingCrawlerWrongException("没有此用户的题目解决信息");
        }
        return res;
    }

    private JsonNode parseLanguageUsedJson(String data) throws RatingCrawlerWrongException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode res;
        try {
            res = mapper.readTree(data).get("data").get("userLanguageProblemCount");
        } catch (Exception e) {
            throw new RatingCrawlerWrongException("解析语言使用信息失败");
        }
        if (res == null) {
            throw new RatingCrawlerWrongException("没有此用户的语言使用信息");
        }
        return res;
    }

    private JsonNode parseProfileJson(String data) throws RatingCrawlerWrongException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode res;
        try {
            res = mapper.readTree(data).get("data").get("userProfilePublicProfile").get("profile");
        } catch (Exception e) {
            throw new RatingCrawlerWrongException("解析用户信息失败");
        }
        if (res == null) {
            throw new RatingCrawlerWrongException("没有此用户信息");
        }
        return res;
    }

    private JsonNode parseRatingChangeJson(String data) throws RatingCrawlerWrongException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode res;
        try {
            res = mapper.readTree(data).get("data").get("userContestRankingHistory");
        } catch (Exception e) {
            throw new RatingCrawlerWrongException("解析排名变化信息失败");
        }
        if (res == null) {
            throw new RatingCrawlerWrongException("没有此用户的排名变化信息");
        }
        return res;
    }

    private JsonNode parseRatingJson(String data) throws RatingCrawlerWrongException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode res;
        try {
            res = mapper.readTree(data).get("data").get("userContestRanking");
        } catch (Exception e) {
            throw new RatingCrawlerWrongException("解析排名信息失败");
        }
        if (res == null) {
            throw new RatingCrawlerWrongException("没有此用户的排名信息");
        }
        return res;
    }

    private JsonNode parseGradingJson(String data) throws RatingCrawlerWrongException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode res;
        try {
            res = mapper.readTree(data).get("data").get("userProfileUserLevelMedal");
        } catch (Exception e) {
            throw new RatingCrawlerWrongException("解析等级信息失败");
        }
        if (res == null) {
            throw new RatingCrawlerWrongException("没有此用户的等级信息");
        }
        return res;
    }

    private LcRatingChangeDto parseRatingChange(JsonNode ratingChangeJson) {
        LcRatingChangeDto ratingChangeDto = new LcRatingChangeDto();
        ratingChangeDto.setAttended(ratingChangeJson.get("attended").asBoolean());
        ratingChangeDto.setTitle(ratingChangeJson.get("contest").get("title").asText());
        ratingChangeDto.setTotalProblems(ratingChangeJson.get("totalProblems").asInt());
        ratingChangeDto.setTrendingDirection(ratingChangeJson.get("trendingDirection").asText());
        ratingChangeDto.setStartTime(ratingChangeJson.get("contest").get("startTime").asLong());
        ratingChangeDto.setFinishTimeInSeconds(ratingChangeJson.get("finishTimeInSeconds").asLong());
        ratingChangeDto.setRating((int)Math.ceil(ratingChangeJson.get("rating").asDouble()));
        ratingChangeDto.setScore(ratingChangeJson.get("score").asInt());
        ratingChangeDto.setRanking(ratingChangeJson.get("ranking").asInt());
        return ratingChangeDto;
    }

    private LcRatingDto parseRating(JsonNode ratingJson, JsonNode gradingJson) {
        LcRatingDto ratingInfo = new LcRatingDto();
        ratingInfo.setRating((int)Math.ceil(ratingJson.has("rating") ? ratingJson.get("rating").asDouble() : 0));
        ratingInfo.setNationRank(ratingJson.has("localRanking") ? ratingJson.get("localRanking").asInt() : 0);
        ratingInfo.setGlobalRank(ratingJson.has("globalRanking") ? ratingJson.get("globalRanking").asInt() : 0);
        ratingInfo.setTopPercentage(ratingJson.has("topPercentage") ? ratingJson.get("topPercentage").asDouble() : 100.0);
        ratingInfo.setGlobalTotalParticipants(ratingJson.has("globalTotalParticipants") ? ratingJson.get("globalTotalParticipants").asInt() : 0);
        ratingInfo.setLocalTotalParticipants(ratingJson.has("localTotalParticipants") ? ratingJson.get("localTotalParticipants").asInt() : 0);
        if (gradingJson.has("current") && gradingJson.get("current").has("name")) {
            ratingInfo.setGrading(gradingJson.get("current").get("name").asText());
        } else {
            ratingInfo.setGrading("null");
        }
        return ratingInfo;
    }

}

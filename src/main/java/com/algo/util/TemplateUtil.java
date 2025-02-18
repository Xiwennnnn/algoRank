package com.algo.util;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.algo.data.dto.codeforces.CfRatingChangeDto;
import com.algo.data.dto.codeforces.CfRatingDto;
import com.algo.data.dto.codeforces.CfStatusDto;
import com.algo.data.dto.leetcode.LanguageAndProblemDto;
import com.algo.data.dto.leetcode.LcRatingDto;
import com.algo.data.dto.leetcode.LcRatingHistoryDto;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateUtil {
    public static final TemplateEngine templateEngine;

    static {
        templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setCharacterEncoding("UTF-8");
        resolver.setSuffix(".html");
        templateEngine.setTemplateResolver(resolver);
    }

    public static String renderCodeforcesRankChange(CfRatingDto profile, List<CfRatingChangeDto> ratingChanges, List<CfStatusDto> statusDtos) {
        Context context = new Context();
        Map<String, Object> data = new HashMap<>();
        data.put("RatingChangesMetaData", ratingChanges);
        data.put("HotSpotMapData", statusDtos);
        data.put("UserInfo", profile);
        data.put("Level", "color-" + profile.getRank().replace(" ", "-"));
        context.setVariables(data);
        return templateEngine.process("templates/card/codeforces_rating_change", context);
    }

    public static String renderMultipleCodeforcesRankChange(List<List<CfRatingChangeDto>> ratingChanges) {
        Context context = new Context();
        Map<String, Object> data = new HashMap<>();
        data.put("MultipleRatingChangesMetaData", ratingChanges);
        context.setVariables(data);
        return templateEngine.process("templates/card/multi_codeforces_rating_change", context);
    }

    public static String renderCodeforcesProfile(CfRatingDto profile, int cnt) {
        Context context = new Context();
        Map<String, Object> data = new HashMap<>();
        data.put("UserInfo", profile);
        data.put("Level", "color-" + profile.getRank().replace(" ", "-"));
        data.put("Count", cnt);
        context.setVariables(data);
        return templateEngine.process("templates/card/codeforces_profile", context);
    }

    public static String renderLeetcodeProfile(LcRatingDto rankChangeInfo, LanguageAndProblemDto languageAndProblemDto) {
        Context context = new Context();
        Map<String, Object> data = new HashMap<>();
        data.put("UserInfo", rankChangeInfo);
        int counts = languageAndProblemDto.getEASY() + languageAndProblemDto.getMEDIUM() + languageAndProblemDto.getHARD();
        data.put("Count", counts);
        context.setVariables(data);
        return templateEngine.process("templates/card/leetcode_profile", context);
    }

    public static String renderLeetcodeRakingChange(LcRatingHistoryDto rankChangeInfo, LanguageAndProblemDto languageAndProblemDto) {
        Context context = new Context();
        Map<String, Object> data = new HashMap<>();
        data.put("RatingChangesMetaData", rankChangeInfo.getRatingChanges());
        data.put("UserInfo", rankChangeInfo.getLcRatingDto());
        data.put("SOLVED_HARD", languageAndProblemDto.getHARD());
        data.put("SOLVED_MEDIUM", languageAndProblemDto.getMEDIUM());
        data.put("SOLVED_EASY", languageAndProblemDto.getEASY());
        data.put("LANGUAGE_DATA", languageAndProblemDto.getLanguageMap());
        context.setVariables(data);
        return templateEngine.process("templates/card/leetcode_rating_change", context);
    }
}

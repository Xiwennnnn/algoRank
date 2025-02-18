package com.algo.bot.data;

import com.algo.data.dao.CfRatingDo;
import com.algo.data.query.CfRatingQuery;
import com.algo.data.query.LcRatingQuery;
import com.algo.data.vo.CfRatingVo;
import com.algo.data.vo.LcRatingVo;
import com.algo.service.RatingService;
import com.algo.util.RedisUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@EnableScheduling
@Component
public class RealNameMap {
    private static final String LEETCODE_REAL_NAME_MAP_KEY = "leetcode_real_name_map:";
    private static final String CODEFORCES_REAL_NAME_MAP_KEY = "codeforces_real_name_map:";
    @Resource
    RatingService ratingService;
    @Resource
    RedisUtil redisUtil;

    @Async
    @Scheduled(fixedDelay = 60 * 1000)
    public void init() {
        List<CfRatingVo> cfRatingVos = ratingService.getCfRatings(CfRatingQuery.empty());
        List<LcRatingVo> lcRatingVos = ratingService.getLcRatings(LcRatingQuery.empty());
        for (CfRatingVo cfRatingVo : cfRatingVos) {
            String realNameKey =  CODEFORCES_REAL_NAME_MAP_KEY + cfRatingVo.getRealName();
            redisUtil.set(realNameKey, cfRatingVo.getUserName(), 60 * 60 * 24);
        }
        for (LcRatingVo lcRatingVo : lcRatingVos) {
            String realNameKey = LEETCODE_REAL_NAME_MAP_KEY + lcRatingVo.getRealName();
            redisUtil.set(realNameKey, lcRatingVo.getUserName(), 60 * 60 * 24);
        }
    }

    public boolean hasLcRealName(String realName) {
        return redisUtil.get(LEETCODE_REAL_NAME_MAP_KEY + realName) != null;
    }

    public String getLcRealName(String realName) {
        return (String)redisUtil.get(LEETCODE_REAL_NAME_MAP_KEY + realName);
    }

    public boolean hasCfRealName(String realName) {
        return redisUtil.get(CODEFORCES_REAL_NAME_MAP_KEY + realName) != null;
    }

    public String getCfRealName(String realName) {
        return (String)redisUtil.get(CODEFORCES_REAL_NAME_MAP_KEY + realName);
    }
}

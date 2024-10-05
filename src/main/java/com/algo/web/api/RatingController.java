package com.algo.web.api;

import com.algo.aspect.ViewAspect;
import com.algo.data.query.CfRatingQuery;
import com.algo.data.query.LcRatingQuery;
import com.algo.data.vo.CfRatingVo;
import com.algo.data.vo.LcRatingVo;
import com.algo.service.RatingService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/rating")
@Slf4j
public class RatingController {
    @Resource
    private RatingService ratingService;

    @GetMapping("/cfs")
    public List<CfRatingVo> getCfRatings(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "major", required = false) String major
    ) {
        CfRatingQuery cfRatingQuery = new CfRatingQuery(name, grade, major);
        return ratingService.getCfRatings(cfRatingQuery);
    }

    @GetMapping("/lcs")
    public List<LcRatingVo> getLcRatings(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "major", required = false) String major
    ) {
        LcRatingQuery lcRatingQuery = new LcRatingQuery(name, grade, major);
        return ratingService.getLcRatings(lcRatingQuery);
    }

    @GetMapping("/lcs/page")
    public Page<LcRatingVo> getLcRatings(
            @RequestParam(value = "current", defaultValue = "0", required = false) Integer current,
            @RequestParam(value = "size", defaultValue = "15", required = false) Integer size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "major", required = false) String major
    ) {
        LcRatingQuery lcRatingQuery = new LcRatingQuery(name, grade, major);
        Page<LcRatingVo> page = new Page<>(current, size);

        return ratingService.getLcRatings(page, lcRatingQuery);
    }

    @GetMapping("/cfs/page")
    public Page<CfRatingVo> getCfRatings(
            @RequestParam(value = "current", defaultValue = "0", required = false) Integer current,
            @RequestParam(value = "size", defaultValue = "15", required = false) Integer size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "major", required = false) String major
    ) {
        CfRatingQuery cfRatingQuery = new CfRatingQuery(name, grade, major);
        System.out.println(current + " " + size + " " + cfRatingQuery);
        Page<CfRatingVo> page = new Page<>(current, size);
        return ratingService.getCfRatings(page, cfRatingQuery);
    }
}

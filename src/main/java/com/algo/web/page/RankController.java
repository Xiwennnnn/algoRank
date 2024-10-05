package com.algo.web.page;

import com.algo.data.common.GradeEnum;
import com.algo.data.common.MajorEnum;
import com.algo.data.vo.CfRatingVo;
import com.algo.data.vo.LcRatingVo;
import com.algo.web.api.RatingController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class RankController {
    @Resource
    RatingController ratingController;
    @RequestMapping("/lcrank")
    public String LeetCodeRank(Model model) {
        Page<LcRatingVo> lcRatings = ratingController.getLcRatings(1, 15, null, null, null);
        List<String> majorList = MajorEnum.getAllMajorNames();
        List<String> gradeList = GradeEnum.getAllGradeEnum();
        model.addAttribute("page", lcRatings);
        model.addAttribute("majorList", majorList);
        model.addAttribute("gradeList", gradeList);
        return "lcrank";
    }

    @RequestMapping("/cfrank")
    public String CodeforcesRank(Model model) {
        Page<CfRatingVo> cfRatings = ratingController.getCfRatings(1, 15, null, null, null);
        List<String> majorList = MajorEnum.getAllMajorNames();
        List<String> gradeList = GradeEnum.getAllGradeEnum();
        model.addAttribute("page", cfRatings);
        model.addAttribute("majorList", majorList);
        model.addAttribute("gradeList", gradeList);
        return "cfrank";
    }
}

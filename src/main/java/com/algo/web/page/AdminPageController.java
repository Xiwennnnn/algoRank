package com.algo.web.page;

import com.algo.data.common.GradeEnum;
import com.algo.data.common.MajorEnum;
import com.algo.data.query.RatingUserQuery;
import com.algo.data.vo.RatingUserVo;
import com.algo.service.RatingService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AdminPageController {

    @Resource
    private RatingService ratingService;

    @RequestMapping("/ratingAdmin")
    public String RatingAdminPage(Model model) {
        Page<RatingUserVo> ratings = ratingService.getRatingUsers(new Page<>(1, 15), new RatingUserQuery());
        List<String> majorList = MajorEnum.getAllMajorNames();
        List<String> gradeList = GradeEnum.getAllGradeEnum();
        model.addAttribute("page", ratings);
        model.addAttribute("majorList", majorList);
        model.addAttribute("gradeList", gradeList);
        return "/admin/ratingAdmin";
    }

    @RequestMapping("/contestAdmin")
    public String ContestAdminPage() {
        return "/admin/contestAdmin";
    }
}

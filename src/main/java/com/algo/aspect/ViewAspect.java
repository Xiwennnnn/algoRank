package com.algo.aspect;

import com.algo.data.vo.CfRatingVo;
import com.algo.data.vo.LcRatingVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ViewAspect {
    private static final String LC_A_LINK_STYLE = " cursor: pointer; position: relative;text-decoration: none;color: #000000; ";
    //private static final String LC_Rank_STYLE = "background: url('/images/%s.png') no-repeat 100 0; background-size: 16px 16px; padding-right: 19px;";


    private static final String CF_LINK_STYLE_TEMPLATE = " cursor: pointer;position: relative; %s text-decoration: none;color: %s; ";
    private static final String ColorTemplate = "<span style='%s color: %s;'>%s</span>";
    private static final String template = "<a style='%s' target='_blank' href='%s'>%s</a>";
    private static final String LeetcodeUserUrl = "https://leetcode.cn/u/";
    private static final String CodeforcesUserUrl = "https://codeforces.com/profile/";

    private static final String fontBold = "font-weight: bold;";

    @Pointcut("execution(* com.algo.web.api.RatingController.getLcRatings(Integer, Integer, String, String, String))")
    public void LcRatingsMethod() {}

    @Pointcut("execution(* com.algo.web.api.RatingController.getCfRatings(Integer, Integer, String, String, String))")
    public void CfRatingsMethod() {}

    @AfterReturning(value = "LcRatingsMethod()", returning = "result")
    public void buildLeetcodeLinks(Object result) {
        for (var rating : ((Page<?>)result).getRecords()) {
            if (rating instanceof LcRatingVo r) {
                String userUrl = LeetcodeUserUrl + r.getUserName();
                r.setUserName(String.format(template,LC_A_LINK_STYLE, userUrl, r.getUserName()));
            }

        }
    }

    @AfterReturning(value = "CfRatingsMethod()", returning = "result")
    public void buildCodeforcesLinks(Object result) {

        for (var rating : ((Page<?>)result).getRecords()) {
            if (rating instanceof CfRatingVo r) {
                String color = ColorRender(r.getRating());
                String maxColor = ColorRender(r.getMaxRating());
                String userUrl = CodeforcesUserUrl + r.getUserName();
                String fontBoldStyle = fontBold;
                if (color.equals("black")) {
                    fontBoldStyle = "";
                }
                String colorLinkStyle = String.format(CF_LINK_STYLE_TEMPLATE, fontBoldStyle, color);
                r.setUserName(String.format(template, colorLinkStyle, userUrl, r.getUserName()));
                r.setRealName(String.format(ColorTemplate, fontBoldStyle, color, r.getRealName()));
                r.setRatingView(String.format(ColorTemplate, fontBoldStyle, color, r.getRating()));
                r.setMaxRatingView(String.format(ColorTemplate, fontBoldStyle, maxColor, r.getMaxRating()));
            }
        }
    }

    public static String ColorRender(Integer rating) {
        if (rating >= 2400) {
            return "red";
        }
        if (rating >= 2100) {
            return "##FF8C00";
        }
        if (rating >= 1900) { //紫名
            return "#aa00aa";
        }
        if (rating >= 1600) {
            return "blue";
        }
        if (rating >= 1400) {//青名
            return "#03A89E";
        }
        if (rating >= 1200) {
            return "green";
        }
        if (rating == 0) {
            return "black";
        }
        return "gray";
    }
}

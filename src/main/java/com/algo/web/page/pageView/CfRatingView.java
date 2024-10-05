package com.algo.web.page.pageView;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CfRatingView {
    private String userName;
    private String realName;
    private String grade;
    private String major;
    private String rating;
    private String cfRank;
    private String maxRating;
}

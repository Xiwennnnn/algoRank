package com.algo.web.page.pageView;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LcRatingView {
    private String userName;
    private String realName;
    private String grade;
    private String major;
    private Integer rating;
    private String grading;
}

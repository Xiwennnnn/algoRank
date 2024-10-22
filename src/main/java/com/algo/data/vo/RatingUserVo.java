package com.algo.data.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class RatingUserVo {
    @ExcelProperty(value = "姓名")
    private String realName;
    @ExcelProperty(value = "年级")
    private String grade;
    @ExcelProperty(value = "专业")
    private String major;
    @ExcelProperty(value = "力扣用户名")
    private String lcUsername;
    @ExcelProperty(value = "CF用户名")
    private String cfUsername;
}

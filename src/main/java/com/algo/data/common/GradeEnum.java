package com.algo.data.common;

import java.util.ArrayList;
import java.util.List;

public enum GradeEnum {

    全部("全部"),
    _24级("24"),
    _23级("23"),
    _22级("22"),
    _21级("21"),
    _20级("20");

    public final String grade;
    GradeEnum(String grade) {
        this.grade = grade;
    }

    public static List<String> getAllGradeEnum() {
        List<String> gradeList = new ArrayList<>();
        for (GradeEnum grade : GradeEnum.values()) {
            gradeList.add(grade.grade);
        }
        return gradeList;
    }
}

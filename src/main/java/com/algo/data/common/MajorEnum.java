package com.algo.data.common;

import java.util.ArrayList;
import java.util.List;

public enum MajorEnum {
    Alls("全部"),
    Computer_Science("计算机科学与技术"),
    Software_Engineering("软件工程"),
    Big_Data("大数据"),
    Artificial_Intelligence("人工智能"),
    Information_Computational_Science("信息与计算科学"),
    Computer_Common("计算机大类");


    public final String Name;

    MajorEnum(String Name) {
        this.Name = Name;
    }

    public static List<String> getAllMajorNames() {
        List<String> majorNames = new ArrayList<>();
        for (MajorEnum major : MajorEnum.values()) {
            majorNames.add(major.Name);
        }
        return majorNames;
    }
}

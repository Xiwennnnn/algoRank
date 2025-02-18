package com.algo.data.common;

import java.util.ArrayList;
import java.util.List;

public enum UserInfoEnum {
    real_name("姓名"),
    grade("年级"),
    major("专业"),
    leetcode_id("力扣"),
    codeforces_id("cf");

    public final String value;
    UserInfoEnum(String value) {
        this.value = value;
    }

    public static List<String> getAllValues() {
        List<String> values = new ArrayList<>();
        for (UserInfoEnum e : UserInfoEnum.values()) {
            values.add(e.value);
        }
        return values;
    }
}

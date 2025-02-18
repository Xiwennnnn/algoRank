package com.algo.data.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum OJEnum {
    LeetCode("https://leetcode.cn/", "LeetCode", "力扣", "https://leetcode-cn.com/contest/"),
    Codeforces("https://codeforces.com/", "Codeforces", "Codeforces", "https://codeforces.com/contest/"),
    NowCoder("https://www.nowcoder.com/", "NowCoder", "牛客", "https://ac.nowcoder.com/acm/contest/index/"),
    LuoGu("https://www.luogu.com.cn/", "LuoGu", "洛谷", "https://www.luogu.com.cn/contest/"),
    WHPU("http://oj.whpu.tech:7788/", "WHPU", "轻工大", "http://oj.whpu.tech:7788/contest/"),
    其他("https://www.baidu.com/", "其他", "其他", "https://www.baidu.com/");

    public final String url;
    public final String name;
    public final String ViewName;
    public final String contestUrl;

    OJEnum(String url, String name, String ViewName, String contestUrl) {
        this.url = url;
        this.name = name;
        this.ViewName = ViewName;
        this.contestUrl = contestUrl;
    }

    public static List<String> getAllOJEnum() {
        List<String> ojList = new ArrayList<>();
        for (OJEnum oj : OJEnum.values()) {
            ojList.add(oj.name);
        }
        return ojList;
    }

    public static Map<String, OJEnum> dialectMap() {
        Map<String, OJEnum> dialectMap = new HashMap<>();
        for (OJEnum oj : OJEnum.values()) {
            dialectMap.put(oj.name, oj);
        }
        dialectMap.put("力扣", LeetCode);
        dialectMap.put("cf", Codeforces);
        dialectMap.put("牛客", NowCoder);
        dialectMap.put("洛谷", LuoGu);
        dialectMap.put("lc", LeetCode);
        dialectMap.put("codeforces", Codeforces);
        return dialectMap;
    }
}

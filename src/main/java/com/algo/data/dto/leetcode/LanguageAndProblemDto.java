package com.algo.data.dto.leetcode;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class LanguageAndProblemDto {
    Map<String, Integer> languageMap = new HashMap<>();
    private Integer EASY;
    private Integer MEDIUM;
    private Integer HARD;
}

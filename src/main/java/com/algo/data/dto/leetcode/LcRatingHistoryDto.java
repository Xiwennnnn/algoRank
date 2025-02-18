package com.algo.data.dto.leetcode;

import lombok.Data;

import java.util.List;

@Data
public class LcRatingHistoryDto {
    private LcRatingDto lcRatingDto;
    private List<LcRatingChangeDto> ratingChanges;
}

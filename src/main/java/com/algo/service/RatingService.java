package com.algo.service;

import com.algo.data.query.CfRatingQuery;
import com.algo.data.query.LcRatingQuery;
import com.algo.data.vo.CfRatingVo;
import com.algo.data.vo.LcRatingVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface RatingService {
    public List<LcRatingVo> getLcRatings(LcRatingQuery query);

    public List<CfRatingVo> getCfRatings(CfRatingQuery query);

    public Page<LcRatingVo> getLcRatings(Page<LcRatingVo> page, LcRatingQuery query);

    public Page<CfRatingVo> getCfRatings(Page<CfRatingVo> page, CfRatingQuery query);
}

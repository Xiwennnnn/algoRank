package com.algo.service;

import com.algo.data.query.CfRatingQuery;
import com.algo.data.query.LcRatingQuery;
import com.algo.data.query.RatingUserQuery;
import com.algo.data.vo.CfRatingVo;
import com.algo.data.vo.LcRatingVo;
import com.algo.data.vo.RatingUserVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RatingService {
    public List<LcRatingVo> getLcRatings(LcRatingQuery query);

    public List<CfRatingVo> getCfRatings(CfRatingQuery query);

    public Page<LcRatingVo> getLcRatings(Page<LcRatingVo> page, LcRatingQuery query);

    public Page<CfRatingVo> getCfRatings(Page<CfRatingVo> page, CfRatingQuery query);

    public Page<RatingUserVo> getRatingUsers(Page<RatingUserVo> page, RatingUserQuery query);

    public Integer addLcRating(String username, String realName);

    public Integer addCfRating(String username, String realName);

    public void deleteRating(String realName);

    public void addAllRating(RatingUserVo ratingUserVo);

    public void updateRating(RatingUserVo ratingUserVo);

    public void upload(MultipartFile file);

    public void save(RatingUserVo ratingUserVo);

}

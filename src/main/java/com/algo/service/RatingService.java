package com.algo.service;

import com.algo.data.dao.AlgoUserDo;
import com.algo.data.query.CfRatingQuery;
import com.algo.data.query.LcRatingQuery;
import com.algo.data.query.RatingUserQuery;
import com.algo.data.vo.CfRatingVo;
import com.algo.data.vo.LcRatingVo;
import com.algo.data.vo.RatingUserVo;
import com.algo.exception.UserModifyException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RatingService {
    public RatingUserVo getUserVoByRealName(String realName);

    public List<LcRatingVo> getLcRatings(LcRatingQuery query);

    public List<CfRatingVo> getCfRatings(CfRatingQuery query);

    public Page<LcRatingVo> getLcRatings(Page<LcRatingVo> page, LcRatingQuery query);

    public Page<CfRatingVo> getCfRatings(Page<CfRatingVo> page, CfRatingQuery query);

    public Page<RatingUserVo> getRatingUsers(Page<RatingUserVo> page, RatingUserQuery query);

    public Integer addLcRating(String username, String realName) throws UserModifyException;

    public Integer addCfRating(String username, String realName) throws UserModifyException;

    public void deleteRating(String realName) throws UserModifyException;

    public void save(RatingUserVo ratingUserVo) throws UserModifyException;

    public void updateRating(RatingUserVo ratingUserVo) throws UserModifyException;

    public void upload(MultipartFile file);

    public AlgoUserDo getByRealName(String realName);

    public AlgoUserDo getByQQ(Long qq);
}

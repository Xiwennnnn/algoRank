package com.algo.service.impl;

import com.algo.data.dao.CfRatingDo;
import com.algo.data.dao.LcRatingDo;
import com.algo.data.query.CfRatingQuery;
import com.algo.data.query.LcRatingQuery;
import com.algo.data.vo.CfRatingVo;
import com.algo.data.vo.LcRatingVo;
import com.algo.data.mapper.AlgoUserMapper;
import com.algo.data.mapper.CfRatingMapper;
import com.algo.data.mapper.LcRatingMapper;
import com.algo.service.RatingService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class RatingServiceImpl implements RatingService {
    @Resource
    private AlgoUserMapper algoUserMapper;
    @Resource
    private LcRatingMapper lcRatingMapper;
    @Resource
    private CfRatingMapper cfRatingMapper;

    @Override
    @Cacheable(
            cacheNames = "lcRatingCache",
            key = "#lcRatingQuery.getName() + '_' + #lcRatingQuery.getMajor() + '_' + #lcRatingQuery.getGrade()"
    )
    public List<LcRatingVo> getLcRatings(LcRatingQuery lcRatingQuery) {
        QueryWrapper<LcRatingDo> wrapper = new QueryWrapper<>();
        if (lcRatingQuery.getName()!= null) {
            wrapper.like("name", lcRatingQuery.getName());
        }
        if (lcRatingQuery.getMajor() != null) {
            wrapper.eq("major", lcRatingQuery.getMajor());
        }
        if (lcRatingQuery.getGrade() != null) {
            wrapper.eq("grade", lcRatingQuery.getGrade());
        }
        return lcRatingMapper.getLcRatingVOs(wrapper);
    }

    @Override
    @Cacheable(
            cacheNames = "cfRatingCache",
            key = "#cfRatingQuery.getName() + '_' + #cfRatingQuery.getMajor() + '_' + #cfRatingQuery.getGrade()"
    )
    public List<CfRatingVo> getCfRatings(CfRatingQuery cfRatingQuery) {
        QueryWrapper<CfRatingDo> wrapper = new QueryWrapper<>();
        if (cfRatingQuery.getName()!= null) {
            wrapper.like("name", cfRatingQuery.getName());
        }
        if (cfRatingQuery.getMajor() != null) {
            wrapper.eq("major", cfRatingQuery.getMajor());
        }
        if (cfRatingQuery.getGrade() != null) {
            wrapper.eq("grade", cfRatingQuery.getGrade());
        }
        return cfRatingMapper.getCfRatingVOs(wrapper);
    }

    @Override
    @Cacheable(
            key = "#page.current + '_' + #page.size + '_' + #lcRatingQuery.getName() + '_' + #lcRatingQuery.getMajor() + '_' + #lcRatingQuery.getGrade()",
            cacheNames = "lcRatingPageCache"
    )
    public Page<LcRatingVo> getLcRatings(Page<LcRatingVo> page, LcRatingQuery lcRatingQuery) {
        QueryWrapper<LcRatingDo> wrapper = new QueryWrapper<>();
        if (lcRatingQuery.getName()!= null) {
            wrapper.like("real_name", lcRatingQuery.getName());
        }
        if (lcRatingQuery.getMajor() != null) {
            wrapper.eq("major", lcRatingQuery.getMajor());
        }
        if (lcRatingQuery.getGrade() != null) {
            wrapper.eq("grade", lcRatingQuery.getGrade());
        }
        return lcRatingMapper.getLcRatingVOs(page, wrapper);
    }

    @Override
    @Cacheable(
            key = "#page.current + '_' + #page.size + '_' + #cfRatingQuery.getName() + '_' + #cfRatingQuery.getMajor() + '_' + #cfRatingQuery.getGrade()",
            cacheNames = "cfRatingPageCache"
    )
    public Page<CfRatingVo> getCfRatings(Page<CfRatingVo> page, CfRatingQuery cfRatingQuery) {
        QueryWrapper<CfRatingDo> wrapper = new QueryWrapper<>();
        if (cfRatingQuery.getName()!= null) {
            wrapper.like("real_name", cfRatingQuery.getName());
        }
        if (cfRatingQuery.getMajor() != null) {
            wrapper.eq("major", cfRatingQuery.getMajor());
        }
        if (cfRatingQuery.getGrade() != null) {
            wrapper.eq("grade", cfRatingQuery.getGrade());
        }
        return cfRatingMapper.getCfRatingVOs(page, wrapper);
    }
}

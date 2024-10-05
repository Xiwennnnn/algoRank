package com.algo.data.mapper;

import com.algo.data.dao.CfRatingDo;
import com.algo.data.vo.CfRatingVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CfRatingMapper extends BaseMapper<CfRatingDo> {
    public List<CfRatingVo> getCfRatingVOs(@Param(Constants.WRAPPER) Wrapper<CfRatingDo> wrapper);

    public Page<CfRatingVo> getCfRatingVOs(Page<CfRatingVo> page, @Param(Constants.WRAPPER) Wrapper<CfRatingDo> wrapper);
}

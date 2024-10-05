package com.algo.data.mapper;

import com.algo.data.dao.LcRatingDo;
import com.algo.data.vo.LcRatingVo;
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
public interface LcRatingMapper extends BaseMapper<LcRatingDo> {
    public List<LcRatingVo> getLcRatingVOs(@Param(Constants.WRAPPER) Wrapper wrapper);

    public Page<LcRatingVo> getLcRatingVOs(Page<LcRatingVo> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}

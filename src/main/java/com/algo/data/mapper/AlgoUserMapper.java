package com.algo.data.mapper;

import com.algo.data.dao.AlgoUserDo;
import com.algo.data.vo.RatingUserVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AlgoUserMapper extends BaseMapper<AlgoUserDo> {

    public Page<RatingUserVo> getRatingUsers(Page<RatingUserVo> page, @Param(Constants.WRAPPER) Wrapper<RatingUserVo> wrapper);
}

package com.algo.data.mapper;

import com.algo.data.dao.ContestDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ContestMapper extends BaseMapper<ContestDo> {
    @Select("SELECT * FROM contest WHERE link = #{link}")
    public ContestDo selectByLink(String link);
}

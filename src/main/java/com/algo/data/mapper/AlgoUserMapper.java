package com.algo.data.mapper;

import com.algo.data.dao.AlgoUserDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AlgoUserMapper extends BaseMapper<AlgoUserDo> {}

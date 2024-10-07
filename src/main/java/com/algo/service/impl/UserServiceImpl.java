package com.algo.service.impl;

import com.algo.data.dao.UserDo;
import com.algo.data.mapper.UserMapper;
import com.algo.data.vo.LoginVo;
import com.algo.data.vo.UserVo;
import com.algo.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public UserVo queryByUsername(String username) {
        UserDo userDo = userMapper.selectOne(Wrappers.<UserDo>query().eq("username", username));
        return UserVo.fromDo(userDo);
    }

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        return Map.of();
    }
}

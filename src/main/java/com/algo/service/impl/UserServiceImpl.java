package com.algo.service.impl;

import cn.hutool.jwt.JWTUtil;
import com.algo.data.dao.UserDo;
import com.algo.data.dto.UserDto;
import com.algo.data.mapper.UserMapper;
import com.algo.data.vo.LoginVo;
import com.algo.data.vo.UserVo;
import com.algo.service.UserService;
import com.algo.util.JwtUtil;
import com.algo.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public UserVo queryByUsername(String username) {
        UserDo userDo = userMapper.selectOne(Wrappers.<UserDo>query().eq("username", username));
        if (Objects.isNull(userDo)) {
            return null;
        }
        return UserVo.fromDo(userDo);
    }
}

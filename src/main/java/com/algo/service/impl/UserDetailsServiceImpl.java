package com.algo.service.impl;

import com.algo.data.dto.UserDto;
import com.algo.data.vo.UserVo;
import com.algo.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVo userVo = userService.queryByUsername(username);
        if (Objects.isNull(userVo)) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        return new UserDto(userVo, Collections.emptyList());
    }
}

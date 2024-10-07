package com.algo.data.dto;

import com.algo.data.dao.UserDo;
import com.algo.data.vo.UserVo;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserDto extends User {
    private UserVo userVo;

    public UserDto(UserVo userDo, Collection<? extends GrantedAuthority> authorities) {
        super(userDo.getUsername(), userDo.getPassword(), authorities);
        this.userVo = userVo;
    }

    public UserVo getUserDo() {
        return userVo;
    }

    public void setUserDo(UserVo userVo) {
        this.userVo = userVo;
    }
}

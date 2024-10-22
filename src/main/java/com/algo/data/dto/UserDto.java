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
        this.userVo = userDo;
    }

    public void setUsername (String username) {
        userVo.setUsername(username);
    }

    public void setPassword (String password) {
        userVo.setPassword(password);
    }

    public void setRole (String role) {
        userVo.setRole(role);
    }

    public void setCaptain (Boolean captain) {
        userVo.setCaptain(captain);
    }

    public String getUsername () {
        return userVo.getUsername();
    }

    public String getPassword () {
        return userVo.getPassword();
    }

    public String getRole () {
        return userVo.getRole();
    }

    public Boolean isCaptain () {
        return userVo.isCaptain();
    }


}

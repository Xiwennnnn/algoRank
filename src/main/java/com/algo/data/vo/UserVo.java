package com.algo.data.vo;

import com.algo.data.dao.UserDo;
import com.algo.data.dto.UserDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserVo implements Serializable {
    private String username;
    private String password;
    private String role;
    private boolean captain;

    public static UserVo fromDo(UserDo userDo) {
        UserVo userVo = new UserVo();
        userVo.setUsername(userDo.getUsername());
        userVo.setPassword(userDo.getPassword());
        userVo.setRole(userDo.getRole());
        userVo.setCaptain(userDo.isCaptain());
        return userVo;
    }

    public static UserVo fromDto(UserDto userDto) {
        UserVo userVo = new UserVo();
        userVo.setUsername(userDto.getUsername());
        userVo.setPassword(userDto.getPassword());
        userVo.setRole(userDto.getRole());
        userVo.setCaptain(userDto.isCaptain());
        return userVo;
    }

}

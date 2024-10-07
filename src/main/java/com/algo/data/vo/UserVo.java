package com.algo.data.vo;

import com.algo.data.dao.UserDo;
import lombok.Data;

@Data
public class UserVo {
    private String username;
    private String password;
    private Boolean isCaptain;

    public static UserVo fromDo(UserDo userDo) {
        UserVo userVo = new UserVo();
        userVo.setUsername(userDo.getUserName());
        userVo.setPassword(userDo.getPassword());
        userVo.setIsCaptain(userDo.getIsCaptain());
        return userVo;
    }
}

package com.algo.data.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVo implements Serializable {
    private String username;
    private String password;
}

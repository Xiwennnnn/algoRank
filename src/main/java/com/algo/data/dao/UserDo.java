package com.algo.data.dao;

import lombok.Data;

@Data
public class UserDo {
    private Integer userId;
    private String userName;
    private String password;
    private Boolean isCaptain;

}

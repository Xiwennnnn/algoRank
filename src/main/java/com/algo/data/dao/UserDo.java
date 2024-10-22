package com.algo.data.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class UserDo {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;
    private String username;
    private String password;
    private String role;
    private boolean captain;
}

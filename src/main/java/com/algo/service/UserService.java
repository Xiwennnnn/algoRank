package com.algo.service;

import com.algo.data.dao.UserDo;
import com.algo.data.vo.LoginVo;
import com.algo.data.vo.UserVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface UserService {
   public UserVo queryByUsername(String username);
}

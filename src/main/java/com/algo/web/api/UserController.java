package com.algo.web.api;

import com.algo.data.vo.LoginVo;
import com.algo.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserServiceImpl userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginVo loginVo) {
        Authentication token = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "success";
    }

}

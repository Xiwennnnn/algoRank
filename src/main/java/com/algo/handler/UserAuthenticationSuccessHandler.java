package com.algo.handler;

import com.algo.data.dto.UserDto;
import com.algo.data.vo.UserVo;
import com.algo.util.JwtUtil;
import com.algo.util.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;

@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(UserAuthenticationSuccessHandler.class);
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDto user = (UserDto)authentication.getPrincipal();
        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        String redisKey = "login:" + user.getUsername();
        String key = Base64.getEncoder().encodeToString(redisKey.getBytes());
        UserVo userVo = UserVo.fromDto(user);
        redisUtil.set(key, userVo, 60 * 60 * 24 * 30);
        response.addCookie(cookie);
        response.setStatus(200);
        response.sendRedirect("/");
    }
}

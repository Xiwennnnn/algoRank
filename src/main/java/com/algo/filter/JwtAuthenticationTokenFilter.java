package com.algo.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.algo.data.dto.UserDto;
import com.algo.data.vo.UserVo;
import com.algo.util.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        // 从Cookie中获取token
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null || token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        String username;
        try {
            JWT jwt = JWTUtil.parseToken(token);
            username = (String) jwt.getPayload("username");
        } catch (Exception e) {
            response.addCookie(new Cookie("token", null));
            response.sendRedirect("/");
            return;
        }
        String key = "login:" + username;
        String redisKey = Base64.getEncoder().encodeToString(key.getBytes());
        UserVo userVo = (UserVo) redisUtil.get(redisKey);

        if (Objects.isNull(userVo)) {
            // 登录已经过期，需要重新登录，并删除Cookie中的token
            response.addCookie(new Cookie("token", null));
            response.sendRedirect("/");
            return;
        }
        log.info(userVo.toString());

        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userVo.getRole());
        UserDto user = new UserDto(userVo, authorities);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}

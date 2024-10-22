package com.algo.config;

import com.algo.handler.UserAuthenticationSuccessHandler;
import com.algo.util.CustomMd5PasswordEncoder;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

/**
 * @author xiwen
 * @version 1.0.0
 * @time 2024/10/7 10:59
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Resource
    private UserAuthenticationSuccessHandler userAuthenticationSuccessHandler;
    /**
     * 自定义密码加密器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new CustomMd5PasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
    /**
     * 获取 AuthenticationManager, 用于认证用户
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 配置安全拦截器
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
//                 禁用 CSRF，因为我们使用 JWT 进行身份验证，不需要依赖于 CSRF 保护
                .csrf().disable()
                // 不创建会话，因为我们使用 JWT 进行身份验证，不需要服务器端的会话
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 哪些路径不需要登录就可以访问
                .authorizeHttpRequests()
//                    .requestMatchers("/api/**").permitAll()
//                    .requestMatchers("/page/**").permitAll()
//                    .requestMatchers("/", "/cfrank", "/lcrank", "/calendar").permitAll()
//                    // 静态资源不需要登录就可以访问
//                    .requestMatchers("/static/**", "/webjars/**", "/css/**", "/js/**", "/images/**", "/icon/**").permitAll()
//                    // 其他路径需要登录才能访问
//                    .anyRequest().authenticated()
                .anyRequest().permitAll()
                .and()
                // 配置登录页面
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/api/user/login")
                    .successHandler(userAuthenticationSuccessHandler).permitAll()
//                  重定向到登录页面，如果登录失败
                    .failureUrl("/login?error=true")
                .and()
                // 设置跨域请求的相关配置
                .cors()
                    .configurationSource(corsConfigurationSource())
                .and()
                .build();
    }


    /**
     * 配置 CORS 跨域请求的相关配置
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration source = new CorsConfiguration();
        // 设置 CORS 跨域请求的相关配置
        // 允许所有请求头跨域请求
        source.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
        // 允许所有请求方法跨域请求
        source.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));
        // 允许所有来源跨域请求
        source.setAllowedOrigins(Collections.singletonList(CorsConfiguration.ALL));
        // 设置是否允许携带凭据（cookies）
        source.setAllowCredentials(true);
        // 设置 CORS 跨域请求的有效期
        source.setMaxAge(3600L);
        // 将 CORS 跨域请求的相关配置添加到 UrlBasedCorsConfigurationSource 中
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        // 将配置添加到 UrlBasedCorsConfigurationSource 中，/** 为通配符，表示对所有请求都生效
        configSource.registerCorsConfiguration("/**", source);
        return configSource;
    }
}

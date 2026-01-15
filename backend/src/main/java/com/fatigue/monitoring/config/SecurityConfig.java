package com.fatigue.monitoring.config;

import com.fatigue.monitoring.security.JwtAccessDeniedHandler;
import com.fatigue.monitoring.security.JwtAuthenticationEntryPoint;
import com.fatigue.monitoring.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 * 配置安全认证和授权规则
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * 密码编码器
     *
     * @return BCrypt密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器
     *
     * @param authenticationConfiguration 认证配置
     * @return 认证管理器
     * @throws Exception 异常
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 安全过滤器链
     *
     * @param http HTTP安全配置
     * @return 安全过滤器链
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF
            .csrf().disable()
            
            // 基于token，不需要session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            
            .and()
            
            // 配置授权规则
            .authorizeHttpRequests()
            // 公开接口
            .antMatchers(
                "/api/auth/login",
                "/api/auth/register",
                "/api/auth/refresh-token",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**",
                "/webjars/**",
                "/doc.html",
                "/favicon.ico",
                "/error"
            ).permitAll()
            // 其他所有请求都需要认证
            .anyRequest().authenticated()
            
            .and()
            
            // 异常处理
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)
            
            .and()
            
            // 添加JWT过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

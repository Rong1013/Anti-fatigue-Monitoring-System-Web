package com.fatigue.monitoring.controller;

import com.fatigue.monitoring.common.result.Result;
import com.fatigue.monitoring.entity.User;
import com.fatigue.monitoring.security.JwtTokenUtil;
import com.fatigue.monitoring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 处理用户登录、注册、刷新令牌等认证相关操作
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    /**
     * 用户登录
     *
     * @param user 用户信息（用户名和密码）
     * @return 登录结果，包含JWT令牌
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        try {
            // 认证用户
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );

            // 获取用户详情
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 生成JWT令牌
            String token = jwtTokenUtil.generateToken(userDetails);

            // 获取用户信息
            User userInfo = userService.selectByUsername(user.getUsername());

            // 构建返回结果
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", userInfo);

            return Result.success(data);
        } catch (Exception e) {
            return Result.error("用户名或密码错误");
        }
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Boolean> register(@Validated @RequestBody User user) {
        try {
            // 检查用户名是否已存在
            User existUser = userService.selectByUsername(user.getUsername());
            if (existUser != null) {
                return Result.error("用户名已存在");
            }

            // 创建用户
            boolean success = userService.createUser(user);
            return success ? Result.success(true) : Result.error("注册失败");
        } catch (Exception e) {
            return Result.error("注册失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/user-info")
    public Result<User> getUserInfo() {
        try {
            // 从SecurityContext中获取当前用户名
            String username = org.springframework.security.core.context.SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            // 获取用户信息
            User user = userService.selectByUsername(username);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error("获取用户信息失败");
        }
    }

    /**
     * 刷新令牌
     *
     * @param token 旧令牌
     * @return 新令牌
     */
    @PostMapping("/refresh-token")
    public Result<Map<String, String>> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            // 去掉"Bearer "前缀
            String jwt = token.replace("Bearer ", "");

            // 验证令牌
            String username = jwtTokenUtil.getUsernameFromToken(jwt);

            // 获取用户详情
            User user = userService.selectByUsername(username);
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();

            // 验证令牌有效性
            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                // 生成新令牌
                String newToken = jwtTokenUtil.generateToken(userDetails);

                Map<String, String> data = new HashMap<>();
                data.put("token", newToken);

                return Result.success(data);
            } else {
                return Result.error("令牌无效或已过期");
            }
        } catch (Exception e) {
            return Result.error("刷新令牌失败");
        }
    }

    /**
     * 用户登出
     *
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        // 清除SecurityContext
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
        return Result.success(true);
    }
}

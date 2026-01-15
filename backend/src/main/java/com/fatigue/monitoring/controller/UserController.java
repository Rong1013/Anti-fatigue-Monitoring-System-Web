package com.fatigue.monitoring.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.monitoring.common.result.Result;
import com.fatigue.monitoring.entity.User;
import com.fatigue.monitoring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<?> getUserInfo(@RequestHeader("userId") Long userId) {
        return Result.success(userService.getUserInfo(userId));
    }

    /**
     * 分页查询用户列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @param user    查询条件
     * @return 用户分页列表
     */
    @GetMapping("/page")
    public Result<Page<User>> selectUserPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            User user) {
        Page<User> page = new Page<>(current, size);
        return Result.success(userService.selectUserPage(page, user));
    }

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<User> selectById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 是否成功
     */
    @PostMapping
    public Result<Boolean> createUser(@Validated @RequestBody User user) {
        return Result.success(userService.createUser(user));
    }

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 是否成功
     */
    @PutMapping
    public Result<Boolean> updateUser(@Validated @RequestBody User user) {
        return Result.success(userService.updateUser(user));
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUser(@PathVariable Long id) {
        return Result.success(userService.deleteUser(id));
    }

    /**
     * 重置用户密码
     *
     * @param id          用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    @PutMapping("/reset-password")
    public Result<Boolean> resetPassword(
            @RequestParam Long id,
            @RequestParam String newPassword) {
        return Result.success(userService.resetPassword(id, newPassword));
    }

    /**
     * 更新用户状态
     *
     * @param id     用户ID
     * @param status 状态
     * @return 是否成功
     */
    @PutMapping("/status")
    public Result<Boolean> updateStatus(
            @RequestParam Long id,
            @RequestParam Integer status) {
        return Result.success(userService.updateStatus(id, status));
    }

    /**
     * 分配用户角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    @PutMapping("/assign-roles")
    public Result<Boolean> assignRoles(
            @RequestParam Long userId,
            @RequestBody List<Long> roleIds) {
        return Result.success(userService.assignRoles(userId, roleIds));
    }
}

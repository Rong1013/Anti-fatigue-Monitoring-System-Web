package com.fatigue.monitoring.controller;

import com.fatigue.monitoring.common.result.Result;
import com.fatigue.monitoring.entity.Role;
import com.fatigue.monitoring.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @GetMapping("/list")
    public Result<List<Role>> selectAllRoles() {
        return Result.success(roleService.selectAllRoles());
    }

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Role>> selectRolesByUserId(@PathVariable Long userId) {
        return Result.success(roleService.selectRolesByUserId(userId));
    }

    /**
     * 根据ID查询角色
     *
     * @param id 角色ID
     * @return 角色信息
     */
    @GetMapping("/{id}")
    public Result<Role> selectById(@PathVariable Long id) {
        return Result.success(roleService.getById(id));
    }

    /**
     * 创建角色
     *
     * @param role 角色信息
     * @return 是否成功
     */
    @PostMapping
    public Result<Boolean> createRole(@Validated @RequestBody Role role) {
        return Result.success(roleService.createRole(role));
    }

    /**
     * 更新角色
     *
     * @param role 角色信息
     * @return 是否成功
     */
    @PutMapping
    public Result<Boolean> updateRole(@Validated @RequestBody Role role) {
        return Result.success(roleService.updateRole(role));
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteRole(@PathVariable Long id) {
        return Result.success(roleService.deleteRole(id));
    }

    /**
     * 更新角色状态
     *
     * @param id     角色ID
     * @param status 状态
     * @return 是否成功
     */
    @PutMapping("/status")
    public Result<Boolean> updateStatus(
            @RequestParam Long id,
            @RequestParam Integer status) {
        return Result.success(roleService.updateStatus(id, status));
    }

    /**
     * 分配角色菜单权限
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     * @return 是否成功
     */
    @PutMapping("/assign-menus")
    public Result<Boolean> assignMenus(
            @RequestParam Long roleId,
            @RequestBody List<Long> menuIds) {
        return Result.success(roleService.assignMenus(roleId, menuIds));
    }
}

package com.fatigue.monitoring.controller;

import com.fatigue.monitoring.common.result.Result;
import com.fatigue.monitoring.entity.Menu;
import com.fatigue.monitoring.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 获取当前用户菜单
     *
     * @param userId 用户ID
     * @return 菜单树
     */
    @GetMapping("/tree")
    public Result<List<Menu>> getMenuTree(@RequestHeader("userId") Long userId) {
        List<Menu> menus = menuService.selectMenusByUserId(userId);
        List<Menu> menuTree = menuService.buildMenuTree(menus);
        return Result.success(menuTree);
    }

    /**
     * 查询所有菜单
     *
     * @return 菜单列表
     */
    @GetMapping("/list")
    public Result<List<Menu>> selectAllMenus() {
        return Result.success(menuService.selectAllMenus());
    }

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @GetMapping("/permissions/{userId}")
    public Result<List<String>> selectPermissionsByUserId(@PathVariable Long userId) {
        return Result.success(menuService.selectPermissionsByUserId(userId));
    }

    /**
     * 根据ID查询菜单
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
    @GetMapping("/{id}")
    public Result<Menu> selectById(@PathVariable Long id) {
        return Result.success(menuService.getById(id));
    }

    /**
     * 创建菜单
     *
     * @param menu 菜单信息
     * @return 是否成功
     */
    @PostMapping
    public Result<Boolean> createMenu(@Validated @RequestBody Menu menu) {
        return Result.success(menuService.createMenu(menu));
    }

    /**
     * 更新菜单
     *
     * @param menu 菜单信息
     * @return 是否成功
     */
    @PutMapping
    public Result<Boolean> updateMenu(@Validated @RequestBody Menu menu) {
        return Result.success(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteMenu(@PathVariable Long id) {
        return Result.success(menuService.deleteMenu(id));
    }

    /**
     * 更新菜单状态
     *
     * @param id     菜单ID
     * @param status 状态
     * @return 是否成功
     */
    @PutMapping("/status")
    public Result<Boolean> updateStatus(
            @RequestParam Long id,
            @RequestParam Integer status) {
        return Result.success(menuService.updateStatus(id, status));
    }
}

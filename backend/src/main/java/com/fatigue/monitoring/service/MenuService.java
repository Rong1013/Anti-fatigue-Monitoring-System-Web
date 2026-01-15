package com.fatigue.monitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fatigue.monitoring.entity.Menu;

import java.util.List;

/**
 * 菜单服务接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
public interface MenuService extends IService<Menu> {

    /**
     * 根据用户ID查询菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenusByUserId(Long userId);

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectPermissionsByUserId(Long userId);

    /**
     * 查询所有菜单
     *
     * @return 菜单列表
     */
    List<Menu> selectAllMenus();

    /**
     * 创建菜单
     *
     * @param menu 菜单信息
     * @return 是否成功
     */
    boolean createMenu(Menu menu);

    /**
     * 更新菜单
     *
     * @param menu 菜单信息
     * @return 是否成功
     */
    boolean updateMenu(Menu menu);

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     * @return 是否成功
     */
    boolean deleteMenu(Long menuId);

    /**
     * 更新菜单状态
     *
     * @param menuId 菜单ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long menuId, Integer status);

    /**
     * 构建菜单树
     *
     * @param menus 菜单列表
     * @return 菜单树
     */
    List<Menu> buildMenuTree(List<Menu> menus);
}

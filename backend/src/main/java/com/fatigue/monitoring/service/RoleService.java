package com.fatigue.monitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fatigue.monitoring.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> selectRolesByUserId(Long userId);

    /**
     * 创建角色
     *
     * @param role 角色信息
     * @return 是否成功
     */
    boolean createRole(Role role);

    /**
     * 更新角色
     *
     * @param role 角色信息
     * @return 是否成功
     */
    boolean updateRole(Role role);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean deleteRole(Long roleId);

    /**
     * 更新角色状态
     *
     * @param roleId 角色ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long roleId, Integer status);

    /**
     * 分配角色菜单权限
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     * @return 是否成功
     */
    boolean assignMenus(Long roleId, List<Long> menuIds);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    List<Role> selectAllRoles();
}

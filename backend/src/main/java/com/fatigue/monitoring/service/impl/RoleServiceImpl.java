package com.fatigue.monitoring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fatigue.monitoring.common.exception.BusinessException;
import com.fatigue.monitoring.entity.Role;
import com.fatigue.monitoring.mapper.RoleMapper;
import com.fatigue.monitoring.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务实现类
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<Role> selectRolesByUserId(Long userId) {
        return baseMapper.selectRolesByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(Role role) {
        // 检查角色标识是否存在
        Role existRole = this.lambdaQuery()
                .eq(Role::getRoleKey, role.getRoleKey())
                .one();
        if (existRole != null) {
            throw new BusinessException("角色标识已存在");
        }

        return this.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(Role role) {
        Role existRole = this.getById(role.getId());
        if (existRole == null) {
            throw new BusinessException("角色不存在");
        }

        return this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long roleId) {
        Role role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        return this.removeById(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long roleId, Integer status) {
        Role role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        role.setStatus(status);
        return this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenus(Long roleId, List<Long> menuIds) {
        Role role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // TODO: 实现分配菜单权限逻辑
        return true;
    }

    @Override
    public List<Role> selectAllRoles() {
        return this.lambdaQuery()
                .eq(Role::getDeleted, 0)
                .list();
    }
}

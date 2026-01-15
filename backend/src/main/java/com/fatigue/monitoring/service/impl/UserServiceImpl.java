package com.fatigue.monitoring.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fatigue.monitoring.common.exception.BusinessException;
import com.fatigue.monitoring.common.utils.JwtUtil;
import com.fatigue.monitoring.dto.LoginDTO;
import com.fatigue.monitoring.entity.Role;
import com.fatigue.monitoring.entity.User;
import com.fatigue.monitoring.mapper.UserMapper;
import com.fatigue.monitoring.service.RoleService;
import com.fatigue.monitoring.service.UserService;
import com.fatigue.monitoring.vo.LoginVO;
import com.fatigue.monitoring.vo.RoleVO;
import com.fatigue.monitoring.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleService roleService;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 根据用户名查询用户
        User user = selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 验证用户状态
        if (user.getStatus() == 1) {
            throw new BusinessException("用户已被禁用");
        }

        // 生成token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 获取用户信息
        UserInfoVO userInfo = getUserInfo(user.getId());

        // 构建登录结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserInfo(userInfo);
        loginVO.setPermissions(userInfo.getPermissions());
        loginVO.setRoles(userInfo.getRoles().stream().map(RoleVO::getRoleKey).collect(Collectors.toList()));

        return loginVO;
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);

        // 查询用户角色
        List<Role> roles = roleService.selectRolesByUserId(userId);
        List<RoleVO> roleVOList = roles.stream().map(role -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            return roleVO;
        }).collect(Collectors.toList());
        userInfoVO.setRoles(roleVOList);

        // 查询用户权限
        List<String> permissions = baseMapper.selectPermissionsByUserId(userId);
        userInfoVO.setPermissions(permissions);

        return userInfoVO;
    }

    @Override
    public User selectByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public Page<User> selectUserPage(Page<User> page, User user) {
        return baseMapper.selectUserPage(page, user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(User user) {
        // 检查用户名是否存在
        User existUser = selectByUsername(user.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        User existUser = this.getById(user.getId());
        if (existUser == null) {
            throw new BusinessException("用户不存在");
        }

        // 如果修改了密码，需要加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }

        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        return this.removeById(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(Long userId, String newPassword) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long userId, Integer status) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        user.setStatus(status);
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // TODO: 实现分配角色逻辑
        return true;
    }
}

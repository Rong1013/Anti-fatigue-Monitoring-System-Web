package com.fatigue.monitoring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fatigue.monitoring.dto.LoginDTO;
import com.fatigue.monitoring.entity.User;
import com.fatigue.monitoring.vo.LoginVO;
import com.fatigue.monitoring.vo.UserInfoVO;

import java.util.List;

/**
 * 用户服务接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoVO getUserInfo(Long userId);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(String username);

    /**
     * 分页查询用户列表
     *
     * @param page    分页参数
     * @param user    查询条件
     * @return 用户分页列表
     */
    Page<User> selectUserPage(Page<User> page, User user);

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 是否成功
     */
    boolean createUser(User user);

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long userId);

    /**
     * 重置用户密码
     *
     * @param userId     用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long userId, Integer status);

    /**
     * 分配用户角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean assignRoles(Long userId, List<Long> roleIds);
}

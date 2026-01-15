package com.fatigue.monitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.monitoring.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询用户角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 分页查询用户列表
     *
     * @param page   分页对象
     * @param user   用户信息
     * @return 用户列表
     */
    IPage<User> selectUserPage(Page<User> page, @Param("user") User user);
}

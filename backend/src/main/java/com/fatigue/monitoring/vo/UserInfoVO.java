package com.fatigue.monitoring.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息VO
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Data
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private String sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色列表
     */
    private List<RoleVO> roles;

    /**
     * 权限列表
     */
    private List<String> permissions;
}

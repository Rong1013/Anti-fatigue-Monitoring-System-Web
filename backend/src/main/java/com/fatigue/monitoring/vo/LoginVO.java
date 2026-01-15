package com.fatigue.monitoring.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 登录VO
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Data
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Token
     */
    private String token;

    /**
     * 用户信息
     */
    private UserInfoVO userInfo;

    /**
     * 权限列表
     */
    private List<String> permissions;

    /**
     * 角色列表
     */
    private List<String> roles;
}

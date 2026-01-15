package com.fatigue.monitoring.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色VO
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Data
public class RoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 角色排序
     */
    private Integer roleSort;

    /**
     * 角色状态
     */
    private Integer status;
}

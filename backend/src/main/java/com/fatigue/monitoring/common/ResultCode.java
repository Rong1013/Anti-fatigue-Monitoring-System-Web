package com.fatigue.monitoring.common;

import lombok.Getter;

/**
 * 返回码枚举
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Getter
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    ERROR(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权，请先登录"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    /**
     * 用户名或密码错误
     */
    LOGIN_ERROR(1001, "用户名或密码错误"),

    /**
     * 用户不存在
     */
    USER_NOT_EXIST(1002, "用户不存在"),

    /**
     * 用户已存在
     */
    USER_EXIST(1003, "用户已存在"),

    /**
     * Token已过期
     */
    TOKEN_EXPIRED(1004, "Token已过期"),

    /**
     * Token无效
     */
    TOKEN_INVALID(1005, "Token无效"),

    /**
     * 设备不存在
     */
    DEVICE_NOT_EXIST(2001, "设备不存在"),

    /**
     * 设备已存在
     */
    DEVICE_EXIST(2002, "设备已存在"),

    /**
     * 产品不存在
     */
    PRODUCT_NOT_EXIST(3001, "产品不存在"),

    /**
     * 任务不存在
     */
    TASK_NOT_EXIST(4001, "任务不存在"),

    /**
     * 告警不存在
     */
    ALARM_NOT_EXIST(5001, "告警不存在"),

    /**
     * 权限不足
     */
    PERMISSION_DENIED(6001, "权限不足"),

    /**
     * 角色不存在
     */
    ROLE_NOT_EXIST(6002, "角色不存在"),

    /**
     * 数据已存在
     */
    DATA_EXIST(7001, "数据已存在");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

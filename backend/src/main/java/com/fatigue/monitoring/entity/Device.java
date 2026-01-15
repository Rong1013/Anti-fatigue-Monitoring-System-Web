package com.fatigue.monitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备实体
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Data
@TableName("device")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * 设备位置
     */
    private String location;

    /**
     * 设备状态（1正常 2异常 3维护中）
     */
    private Integer status;

    /**
     * 运行时长（小时）
     */
    private BigDecimal runTime;

    /**
     * 负载率
     */
    private BigDecimal loadRate;

    /**
     * 温度（℃）
     */
    private BigDecimal temperature;

    /**
     * 振动值
     */
    private BigDecimal vibration;

    /**
     * 压力
     */
    private BigDecimal pressure;

    /**
     * 转速
     */
    private BigDecimal speed;

    /**
     * 最后维护时间
     */
    private LocalDateTime lastMaintenanceTime;

    /**
     * 下次维护时间
     */
    private LocalDateTime nextMaintenanceTime;

    /**
     * 设备描述
     */
    private String description;

    /**
     * 删除标志（0未删除 1已删除）
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
}

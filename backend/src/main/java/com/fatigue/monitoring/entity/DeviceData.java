package com.fatigue.monitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备数据实体
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Data
@TableName("device_data")
public class DeviceData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String deviceCode;

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
     * 负载率
     */
    private BigDecimal loadRate;

    /**
     * 电流
     */
    private BigDecimal current;

    /**
     * 电压
     */
    private BigDecimal voltage;

    /**
     * 功率
     */
    private BigDecimal power;

    /**
     * 数据采集时间
     */
    private LocalDateTime collectTime;

    /**
     * 删除标志（0未删除 1已删除）
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

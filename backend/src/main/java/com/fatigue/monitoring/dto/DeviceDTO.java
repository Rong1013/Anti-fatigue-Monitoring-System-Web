package com.fatigue.monitoring.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备DTO
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Data
public class DeviceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    private Long id;

    /**
     * 设备编号
     */
    @NotBlank(message = "设备编号不能为空")
    private String deviceCode;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空")
    private String deviceName;

    /**
     * 设备类型
     */
    @NotBlank(message = "设备类型不能为空")
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
     * 备注
     */
    private String remark;
}

package com.fatigue.monitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 告警实体
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Data
@TableName("alarm")
public class Alarm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 告警ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 告警编号
     */
    private String alarmCode;

    /**
     * 告警名称
     */
    private String alarmName;

    /**
     * 告警类型
     */
    private String alarmType;

    /**
     * 告警级别（1低 2中 3高 4紧急）
     */
    private Integer level;

    /**
     * 告警内容
     */
    private String content;

    /**
     * 关联设备ID
     */
    private Long deviceId;

    /**
     * 关联设备名称
     */
    private String deviceName;

    /**
     * 告警状态（1未处理 2处理中 3已处理 4已忽略）
     */
    private Integer status;

    /**
     * 告警时间
     */
    private LocalDateTime alarmTime;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理人姓名
     */
    private String handlerName;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 处理结果
     */
    private String handleResult;

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

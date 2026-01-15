package com.fatigue.monitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务实体
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Data
@TableName("task")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务编号
     */
    private String taskCode;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 负责人ID
     */
    private Long assigneeId;

    /**
     * 负责人姓名
     */
    private String assigneeName;

    /**
     * 关联设备ID
     */
    private Long deviceId;

    /**
     * 关联设备名称
     */
    private String deviceName;

    /**
     * 任务状态（1待处理 2进行中 3已完成 4已取消）
     */
    private Integer status;

    /**
     * 优先级（1低 2中 3高）
     */
    private Integer priority;

    /**
     * 计划开始时间
     */
    private LocalDateTime planStartTime;

    /**
     * 计划结束时间
     */
    private LocalDateTime planEndTime;

    /**
     * 实际开始时间
     */
    private LocalDateTime actualStartTime;

    /**
     * 实际结束时间
     */
    private LocalDateTime actualEndTime;

    /**
     * 完成进度
     */
    private Integer progress;

    /**
     * 任务结果
     */
    private String result;

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

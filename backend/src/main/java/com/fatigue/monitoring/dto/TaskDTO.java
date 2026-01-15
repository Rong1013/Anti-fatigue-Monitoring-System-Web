package com.fatigue.monitoring.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务DTO
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Data
public class TaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private Long id;

    /**
     * 任务编号
     */
    @NotBlank(message = "任务编号不能为空")
    private String taskCode;

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    private String taskName;

    /**
     * 任务类型
     */
    @NotBlank(message = "任务类型不能为空")
    private String taskType;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 负责人ID
     */
    @NotNull(message = "负责人不能为空")
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
    @NotNull(message = "计划开始时间不能为空")
    private LocalDateTime planStartTime;

    /**
     * 计划结束时间
     */
    @NotNull(message = "计划结束时间不能为空")
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
     * 备注
     */
    private String remark;
}

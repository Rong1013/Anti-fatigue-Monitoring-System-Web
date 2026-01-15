package com.fatigue.monitoring.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.monitoring.common.result.Result;
import com.fatigue.monitoring.entity.Alarm;
import com.fatigue.monitoring.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 告警控制器
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    /**
     * 分页查询告警列表
     *
     * @param current   当前页
     * @param size      每页大小
     * @param alarm     查询条件
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 告警分页列表
     */
    @GetMapping("/page")
    public Result<Page<Alarm>> selectAlarmPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            Alarm alarm,
            String startTime,
            String endTime) {
        Page<Alarm> page = new Page<>(current, size);
        return Result.success(alarmService.selectAlarmPage(page, alarm, startTime, endTime));
    }

    /**
     * 根据ID查询告警
     *
     * @param id 告警ID
     * @return 告警信息
     */
    @GetMapping("/{id}")
    public Result<Alarm> selectById(@PathVariable Long id) {
        return Result.success(alarmService.getById(id));
    }

    /**
     * 创建告警
     *
     * @param alarm 告警信息
     * @return 是否成功
     */
    @PostMapping
    public Result<Boolean> createAlarm(@Validated @RequestBody Alarm alarm) {
        return Result.success(alarmService.createAlarm(alarm));
    }

    /**
     * 更新告警
     *
     * @param alarm 告警信息
     * @return 是否成功
     */
    @PutMapping
    public Result<Boolean> updateAlarm(@Validated @RequestBody Alarm alarm) {
        return Result.success(alarmService.updateAlarm(alarm));
    }

    /**
     * 删除告警
     *
     * @param id 告警ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteAlarm(@PathVariable Long id) {
        return Result.success(alarmService.deleteAlarm(id));
    }

    /**
     * 处理告警
     *
     * @param id          告警ID
     * @param handleUserId 处理人ID
     * @param handleRemark 处理备注
     * @return 是否成功
     */
    @PutMapping("/handle")
    public Result<Boolean> handleAlarm(
            @RequestParam Long id,
            @RequestParam Long handleUserId,
            @RequestParam(required = false) String handleRemark) {
        return Result.success(alarmService.handleAlarm(id, handleUserId, handleRemark));
    }

    /**
     * 批量处理告警
     *
     * @param alarmIds     告警ID列表
     * @param handleUserId 处理人ID
     * @param handleRemark 处理备注
     * @return 是否成功
     */
    @PutMapping("/batch-handle")
    public Result<Boolean> batchHandleAlarm(
            @RequestBody List<Long> alarmIds,
            @RequestParam Long handleUserId,
            @RequestParam(required = false) String handleRemark) {
        return Result.success(alarmService.batchHandleAlarm(alarmIds, handleUserId, handleRemark));
    }

    /**
     * 统计告警数量
     *
     * @param alarmType  告警类型
     * @param alarmLevel 告警级别
     * @return 告警数量
     */
    @GetMapping("/count")
    public Result<Long> countAlarm(
            @RequestParam(required = false) String alarmType,
            @RequestParam(required = false) String alarmLevel) {
        return Result.success(alarmService.countAlarm(alarmType, alarmLevel));
    }
}

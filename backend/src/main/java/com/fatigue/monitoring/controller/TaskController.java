package com.fatigue.monitoring.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.monitoring.common.result.Result;
import com.fatigue.monitoring.entity.Task;
import com.fatigue.monitoring.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 任务控制器
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * 分页查询任务列表
     *
     * @param current   当前页
     * @param size      每页大小
     * @param task      查询条件
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 任务分页列表
     */
    @GetMapping("/page")
    public Result<Page<Task>> selectTaskPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            Task task,
            String startTime,
            String endTime) {
        Page<Task> page = new Page<>(current, size);
        return Result.success(taskService.selectTaskPage(page, task, startTime, endTime));
    }

    /**
     * 根据ID查询任务
     *
     * @param id 任务ID
     * @return 任务信息
     */
    @GetMapping("/{id}")
    public Result<Task> selectById(@PathVariable Long id) {
        return Result.success(taskService.getById(id));
    }

    /**
     * 创建任务
     *
     * @param task 任务信息
     * @return 是否成功
     */
    @PostMapping
    public Result<Boolean> createTask(@Validated @RequestBody Task task) {
        return Result.success(taskService.createTask(task));
    }

    /**
     * 更新任务
     *
     * @param task 任务信息
     * @return 是否成功
     */
    @PutMapping
    public Result<Boolean> updateTask(@Validated @RequestBody Task task) {
        return Result.success(taskService.updateTask(task));
    }

    /**
     * 删除任务
     *
     * @param id 任务ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteTask(@PathVariable Long id) {
        return Result.success(taskService.deleteTask(id));
    }

    /**
     * 开始任务
     *
     * @param id 任务ID
     * @return 是否成功
     */
    @PutMapping("/start")
    public Result<Boolean> startTask(@RequestParam Long id) {
        return Result.success(taskService.startTask(id));
    }

    /**
     * 完成任务
     *
     * @param id 任务ID
     * @return 是否成功
     */
    @PutMapping("/complete")
    public Result<Boolean> completeTask(@RequestParam Long id) {
        return Result.success(taskService.completeTask(id));
    }

    /**
     * 更新任务进度
     *
     * @param id       任务ID
     * @param progress 进度
     * @return 是否成功
     */
    @PutMapping("/progress")
    public Result<Boolean> updateProgress(
            @RequestParam Long id,
            @RequestParam Integer progress) {
        return Result.success(taskService.updateProgress(id, progress));
    }

    /**
     * 统计任务数量
     *
     * @param taskStatus 任务状态
     * @return 任务数量
     */
    @GetMapping("/count")
    public Result<Long> countTask(@RequestParam(required = false) String taskStatus) {
        return Result.success(taskService.countTask(taskStatus));
    }
}

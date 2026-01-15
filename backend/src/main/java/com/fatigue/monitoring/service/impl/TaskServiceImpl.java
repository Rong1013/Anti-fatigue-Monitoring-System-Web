package com.fatigue.monitoring.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fatigue.monitoring.common.exception.BusinessException;
import com.fatigue.monitoring.entity.Task;
import com.fatigue.monitoring.mapper.TaskMapper;
import com.fatigue.monitoring.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 任务服务实现类
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Override
    public Page<Task> selectTaskPage(Page<Task> page, Task task, String startTime, String endTime) {
        return baseMapper.selectTaskPage(page, task, startTime, endTime);
    }

    @Override
    public Task selectByTaskCode(String taskCode) {
        return baseMapper.selectByTaskCode(taskCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTask(Task task) {
        // 检查任务编号是否存在
        Task existTask = selectByTaskCode(task.getTaskCode());
        if (existTask != null) {
            throw new BusinessException("任务编号已存在");
        }

        // 设置任务状态为待开始
        if (task.getTaskStatus() == null) {
            task.setTaskStatus("0");
        }

        // 设置进度为0
        if (task.getProgress() == null) {
            task.setProgress(0);
        }

        return this.save(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTask(Task task) {
        Task existTask = this.getById(task.getId());
        if (existTask == null) {
            throw new BusinessException("任务不存在");
        }

        return this.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTask(Long taskId) {
        Task task = this.getById(taskId);
        if (task == null) {
            throw new BusinessException("任务不存在");
        }

        // 检查任务状态
        if ("1".equals(task.getTaskStatus()) || "2".equals(task.getTaskStatus())) {
            throw new BusinessException("任务进行中或已完成，无法删除");
        }

        return this.removeById(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startTask(Long taskId) {
        Task task = this.getById(taskId);
        if (task == null) {
            throw new BusinessException("任务不存在");
        }

        // 检查任务状态
        if (!"0".equals(task.getTaskStatus())) {
            throw new BusinessException("任务状态不正确，无法开始");
        }

        // 更新任务状态和实际开始时间
        task.setTaskStatus("1");
        task.setActualStartTime(new Date());

        return this.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeTask(Long taskId) {
        Task task = this.getById(taskId);
        if (task == null) {
            throw new BusinessException("任务不存在");
        }

        // 检查任务状态
        if (!"1".equals(task.getTaskStatus())) {
            throw new BusinessException("任务状态不正确，无法完成");
        }

        // 更新任务状态、实际结束时间和进度
        task.setTaskStatus("2");
        task.setActualEndTime(new Date());
        task.setProgress(100);

        return this.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProgress(Long taskId, Integer progress) {
        Task task = this.getById(taskId);
        if (task == null) {
            throw new BusinessException("任务不存在");
        }

        // 验证进度值
        if (progress < 0 || progress > 100) {
            throw new BusinessException("进度值必须在0-100之间");
        }

        task.setProgress(progress);
        return this.updateById(task);
    }

    @Override
    public Long countTask(String taskStatus) {
        return this.lambdaQuery()
                .eq(taskStatus != null && !taskStatus.isEmpty(), Task::getTaskStatus, taskStatus)
                .count();
    }
}

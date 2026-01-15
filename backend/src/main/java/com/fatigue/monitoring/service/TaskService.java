package com.fatigue.monitoring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fatigue.monitoring.entity.Task;

import java.util.List;

/**
 * 任务服务接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
public interface TaskService extends IService<Task> {

    /**
     * 分页查询任务列表
     *
     * @param page     分页参数
     * @param task     查询条件
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 任务分页列表
     */
    Page<Task> selectTaskPage(Page<Task> page, Task task, String startTime, String endTime);

    /**
     * 根据任务编号查询任务
     *
     * @param taskCode 任务编号
     * @return 任务信息
     */
    Task selectByTaskCode(String taskCode);

    /**
     * 创建任务
     *
     * @param task 任务信息
     * @return 是否成功
     */
    boolean createTask(Task task);

    /**
     * 更新任务
     *
     * @param task 任务信息
     * @return 是否成功
     */
    boolean updateTask(Task task);

    /**
     * 删除任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean deleteTask(Long taskId);

    /**
     * 开始任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean startTask(Long taskId);

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean completeTask(Long taskId);

    /**
     * 更新任务进度
     *
     * @param taskId   任务ID
     * @param progress 进度
     * @return 是否成功
     */
    boolean updateProgress(Long taskId, Integer progress);

    /**
     * 统计任务数量
     *
     * @param taskStatus 任务状态
     * @return 任务数量
     */
    Long countTask(String taskStatus);
}

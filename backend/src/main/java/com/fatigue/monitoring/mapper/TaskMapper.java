package com.fatigue.monitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.monitoring.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 任务Mapper接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    /**
     * 分页查询任务列表
     *
     * @param page 分页对象
     * @param task 任务信息
     * @return 任务列表
     */
    IPage<Task> selectTaskPage(Page<Task> page, @Param("task") Task task);

    /**
     * 根据任务编号查询任务
     *
     * @param taskCode 任务编号
     * @return 任务信息
     */
    Task selectByTaskCode(@Param("taskCode") String taskCode);
}

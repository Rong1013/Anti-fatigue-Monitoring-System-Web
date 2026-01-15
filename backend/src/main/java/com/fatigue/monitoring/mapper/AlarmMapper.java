package com.fatigue.monitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.monitoring.entity.Alarm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 告警Mapper接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Mapper
public interface AlarmMapper extends BaseMapper<Alarm> {

    /**
     * 分页查询告警列表
     *
     * @param page  分页对象
     * @param alarm 告警信息
     * @return 告警列表
     */
    IPage<Alarm> selectAlarmPage(Page<Alarm> page, @Param("alarm") Alarm alarm);

    /**
     * 根据告警编号查询告警
     *
     * @param alarmCode 告警编号
     * @return 告警信息
     */
    Alarm selectByAlarmCode(@Param("alarmCode") String alarmCode);
}

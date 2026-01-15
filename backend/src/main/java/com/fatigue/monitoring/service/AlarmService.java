package com.fatigue.monitoring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fatigue.monitoring.entity.Alarm;

import java.util.Date;

/**
 * 告警服务接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
public interface AlarmService extends IService<Alarm> {

    /**
     * 分页查询告警列表
     *
     * @param page     分页参数
     * @param alarm    查询条件
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 告警分页列表
     */
    Page<Alarm> selectAlarmPage(Page<Alarm> page, Alarm alarm, String startTime, String endTime);

    /**
     * 根据告警编号查询告警
     *
     * @param alarmCode 告警编号
     * @return 告警信息
     */
    Alarm selectByAlarmCode(String alarmCode);

    /**
     * 创建告警
     *
     * @param alarm 告警信息
     * @return 是否成功
     */
    boolean createAlarm(Alarm alarm);

    /**
     * 更新告警
     *
     * @param alarm 告警信息
     * @return 是否成功
     */
    boolean updateAlarm(Alarm alarm);

    /**
     * 删除告警
     *
     * @param alarmId 告警ID
     * @return 是否成功
     */
    boolean deleteAlarm(Long alarmId);

    /**
     * 处理告警
     *
     * @param alarmId      告警ID
     * @param handleUserId 处理人ID
     * @param handleRemark 处理备注
     * @return 是否成功
     */
    boolean handleAlarm(Long alarmId, Long handleUserId, String handleRemark);

    /**
     * 批量处理告警
     *
     * @param alarmIds     告警ID列表
     * @param handleUserId 处理人ID
     * @param handleRemark 处理备注
     * @return 是否成功
     */
    boolean batchHandleAlarm(List<Long> alarmIds, Long handleUserId, String handleRemark);

    /**
     * 统计告警数量
     *
     * @param alarmType 告警类型
     * @param alarmLevel 告警级别
     * @return 告警数量
     */
    Long countAlarm(String alarmType, String alarmLevel);
}

package com.fatigue.monitoring.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fatigue.monitoring.common.exception.BusinessException;
import com.fatigue.monitoring.entity.Alarm;
import com.fatigue.monitoring.mapper.AlarmMapper;
import com.fatigue.monitoring.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 告警服务实现类
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService {

    @Override
    public Page<Alarm> selectAlarmPage(Page<Alarm> page, Alarm alarm, String startTime, String endTime) {
        return baseMapper.selectAlarmPage(page, alarm, startTime, endTime);
    }

    @Override
    public Alarm selectByAlarmCode(String alarmCode) {
        return baseMapper.selectByAlarmCode(alarmCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createAlarm(Alarm alarm) {
        // 检查告警编号是否存在
        Alarm existAlarm = selectByAlarmCode(alarm.getAlarmCode());
        if (existAlarm != null) {
            throw new BusinessException("告警编号已存在");
        }

        // 设置告警时间
        if (alarm.getAlarmTime() == null) {
            alarm.setAlarmTime(new Date());
        }

        // 设置处理状态为未处理
        if (alarm.getHandleStatus() == null) {
            alarm.setHandleStatus(0);
        }

        return this.save(alarm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAlarm(Alarm alarm) {
        Alarm existAlarm = this.getById(alarm.getId());
        if (existAlarm == null) {
            throw new BusinessException("告警不存在");
        }

        return this.updateById(alarm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAlarm(Long alarmId) {
        Alarm alarm = this.getById(alarmId);
        if (alarm == null) {
            throw new BusinessException("告警不存在");
        }

        return this.removeById(alarmId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleAlarm(Long alarmId, Long handleUserId, String handleRemark) {
        Alarm alarm = this.getById(alarmId);
        if (alarm == null) {
            throw new BusinessException("告警不存在");
        }

        // 更新告警处理信息
        alarm.setHandleStatus(1);
        alarm.setHandleUserId(handleUserId);
        alarm.setHandleTime(new Date());
        alarm.setHandleRemark(handleRemark);

        return this.updateById(alarm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchHandleAlarm(List<Long> alarmIds, Long handleUserId, String handleRemark) {
        for (Long alarmId : alarmIds) {
            handleAlarm(alarmId, handleUserId, handleRemark);
        }
        return true;
    }

    @Override
    public Long countAlarm(String alarmType, String alarmLevel) {
        return this.lambdaQuery()
                .eq(alarmType != null && !alarmType.isEmpty(), Alarm::getAlarmType, alarmType)
                .eq(alarmLevel != null && !alarmLevel.isEmpty(), Alarm::getAlarmLevel, alarmLevel)
                .count();
    }
}

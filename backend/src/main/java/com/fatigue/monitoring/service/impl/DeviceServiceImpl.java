package com.fatigue.monitoring.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fatigue.monitoring.common.exception.BusinessException;
import com.fatigue.monitoring.entity.Device;
import com.fatigue.monitoring.mapper.DeviceMapper;
import com.fatigue.monitoring.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 设备服务实现类
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    @Override
    public Page<Device> selectDevicePage(Page<Device> page, Device device) {
        return baseMapper.selectDevicePage(page, device);
    }

    @Override
    public Device selectByDeviceCode(String deviceCode) {
        return baseMapper.selectByDeviceCode(deviceCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDevice(Device device) {
        // 检查设备编号是否存在
        Device existDevice = selectByDeviceCode(device.getDeviceCode());
        if (existDevice != null) {
            throw new BusinessException("设备编号已存在");
        }

        // 设置状态为正常
        if (device.getStatus() == null) {
            device.setStatus(0);
        }

        return this.save(device);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDevice(Device device) {
        Device existDevice = this.getById(device.getId());
        if (existDevice == null) {
            throw new BusinessException("设备不存在");
        }

        return this.updateById(device);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDevice(Long deviceId) {
        Device device = this.getById(deviceId);
        if (device == null) {
            throw new BusinessException("设备不存在");
        }

        return this.removeById(deviceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long deviceId, Integer status) {
        Device device = this.getById(deviceId);
        if (device == null) {
            throw new BusinessException("设备不存在");
        }

        device.setStatus(status);
        return this.updateById(device);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOnlineTime(Long deviceId) {
        Device device = this.getById(deviceId);
        if (device == null) {
            throw new BusinessException("设备不存在");
        }

        device.setLastOnlineTime(new Date());
        device.setStatus(0); // 设置为在线状态
        return this.updateById(device);
    }

    @Override
    public Long countDevice(Integer status) {
        return this.lambdaQuery()
                .eq(status != null, Device::getStatus, status)
                .count();
    }
}

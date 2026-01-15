package com.fatigue.monitoring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fatigue.monitoring.entity.Device;

/**
 * 设备服务接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
public interface DeviceService extends IService<Device> {

    /**
     * 分页查询设备列表
     *
     * @param page   分页参数
     * @param device 查询条件
     * @return 设备分页列表
     */
    Page<Device> selectDevicePage(Page<Device> page, Device device);

    /**
     * 根据设备编号查询设备
     *
     * @param deviceCode 设备编号
     * @return 设备信息
     */
    Device selectByDeviceCode(String deviceCode);

    /**
     * 创建设备
     *
     * @param device 设备信息
     * @return 是否成功
     */
    boolean createDevice(Device device);

    /**
     * 更新设备
     *
     * @param device 设备信息
     * @return 是否成功
     */
    boolean updateDevice(Device device);

    /**
     * 删除设备
     *
     * @param deviceId 设备ID
     * @return 是否成功
     */
    boolean deleteDevice(Long deviceId);

    /**
     * 更新设备状态
     *
     * @param deviceId 设备ID
     * @param status   状态
     * @return 是否成功
     */
    boolean updateStatus(Long deviceId, Integer status);

    /**
     * 更新设备在线时间
     *
     * @param deviceId 设备ID
     * @return 是否成功
     */
    boolean updateOnlineTime(Long deviceId);

    /**
     * 统计设备数量
     *
     * @param status 设备状态
     * @return 设备数量
     */
    Long countDevice(Integer status);
}

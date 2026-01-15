package com.fatigue.monitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.monitoring.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备Mapper接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {

    /**
     * 分页查询设备列表
     *
     * @param page   分页对象
     * @param device 设备信息
     * @return 设备列表
     */
    IPage<Device> selectDevicePage(Page<Device> page, @Param("device") Device device);

    /**
     * 根据设备编号查询设备
     *
     * @param deviceCode 设备编号
     * @return 设备信息
     */
    Device selectByDeviceCode(@Param("deviceCode") String deviceCode);
}

package com.fatigue.monitoring.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.monitoring.common.result.Result;
import com.fatigue.monitoring.entity.Device;
import com.fatigue.monitoring.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 设备控制器
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * 分页查询设备列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @param device  查询条件
     * @return 设备分页列表
     */
    @GetMapping("/page")
    public Result<Page<Device>> selectDevicePage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            Device device) {
        Page<Device> page = new Page<>(current, size);
        return Result.success(deviceService.selectDevicePage(page, device));
    }

    /**
     * 根据ID查询设备
     *
     * @param id 设备ID
     * @return 设备信息
     */
    @GetMapping("/{id}")
    public Result<Device> selectById(@PathVariable Long id) {
        return Result.success(deviceService.getById(id));
    }

    /**
     * 创建设备
     *
     * @param device 设备信息
     * @return 是否成功
     */
    @PostMapping
    public Result<Boolean> createDevice(@Validated @RequestBody Device device) {
        return Result.success(deviceService.createDevice(device));
    }

    /**
     * 更新设备
     *
     * @param device 设备信息
     * @return 是否成功
     */
    @PutMapping
    public Result<Boolean> updateDevice(@Validated @RequestBody Device device) {
        return Result.success(deviceService.updateDevice(device));
    }

    /**
     * 删除设备
     *
     * @param id 设备ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteDevice(@PathVariable Long id) {
        return Result.success(deviceService.deleteDevice(id));
    }

    /**
     * 更新设备状态
     *
     * @param id     设备ID
     * @param status 状态
     * @return 是否成功
     */
    @PutMapping("/status")
    public Result<Boolean> updateStatus(
            @RequestParam Long id,
            @RequestParam String status) {
        return Result.success(deviceService.updateStatus(id, status));
    }

    /**
     * 更新设备在线时间
     *
     * @param id 设备ID
     * @return 是否成功
     */
    @PutMapping("/online-time")
    public Result<Boolean> updateOnlineTime(@RequestParam Long id) {
        return Result.success(deviceService.updateOnlineTime(id));
    }

    /**
     * 统计设备数量
     *
     * @param status 设备状态
     * @return 设备数量
     */
    @GetMapping("/count")
    public Result<Long> countDevice(@RequestParam(required = false) String status) {
        return Result.success(deviceService.countDevice(status));
    }
}

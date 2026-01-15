-- =============================================
-- 疲劳监测系统数据库初始化脚本
-- 数据库版本：1.0.0
-- 创建日期：2024-01-01
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `fatigue_monitoring` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `fatigue_monitoring`;

-- =============================================
-- 用户表
-- =============================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `status` CHAR(1) DEFAULT '1' COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_email` (`email`),
    KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =============================================
-- 角色表
-- =============================================
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `status` CHAR(1) DEFAULT '1' COMMENT '状态（0禁用 1正常）',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- =============================================
-- 菜单表
-- =============================================
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
    `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `menu_type` CHAR(1) DEFAULT 'M' COMMENT '菜单类型（M目录 C菜单 F按钮）',
    `menu_code` VARCHAR(100) DEFAULT NULL COMMENT '菜单编码',
    `path` VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
    `component` VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
    `icon` VARCHAR(100) DEFAULT NULL COMMENT '菜单图标',
    `permission` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `visible` CHAR(1) DEFAULT '1' COMMENT '是否显示（0隐藏 1显示）',
    `status` CHAR(1) DEFAULT '1' COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

-- =============================================
-- 用户角色关联表
-- =============================================
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- =============================================
-- 角色菜单关联表
-- =============================================
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- =============================================
-- 设备表
-- =============================================
DROP TABLE IF EXISTS `biz_device`;
CREATE TABLE `biz_device` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '设备ID',
    `device_code` VARCHAR(50) NOT NULL COMMENT '设备编号',
    `device_name` VARCHAR(100) NOT NULL COMMENT '设备名称',
    `device_type` VARCHAR(50) DEFAULT NULL COMMENT '设备类型',
    `device_model` VARCHAR(100) DEFAULT NULL COMMENT '设备型号',
    `manufacturer` VARCHAR(100) DEFAULT NULL COMMENT '制造商',
    `location` VARCHAR(200) DEFAULT NULL COMMENT '安装位置',
    `status` CHAR(1) DEFAULT '1' COMMENT '状态（0离线 1在线 2维护）',
    `online_time` DATETIME DEFAULT NULL COMMENT '最后在线时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_device_code` (`device_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备表';

-- =============================================
-- 产品表
-- =============================================
DROP TABLE IF EXISTS `biz_product`;
CREATE TABLE `biz_product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '产品ID',
    `product_code` VARCHAR(50) NOT NULL COMMENT '产品编号',
    `product_name` VARCHAR(100) NOT NULL COMMENT '产品名称',
    `product_type` VARCHAR(50) DEFAULT NULL COMMENT '产品类型',
    `specification` VARCHAR(200) DEFAULT NULL COMMENT '规格型号',
    `unit` VARCHAR(20) DEFAULT NULL COMMENT '单位',
    `quantity` INT DEFAULT 0 COMMENT '数量',
    `price` DECIMAL(10, 2) DEFAULT NULL COMMENT '单价',
    `status` CHAR(1) DEFAULT '1' COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_code` (`product_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表';

-- =============================================
-- 任务表
-- =============================================
DROP TABLE IF EXISTS `biz_task`;
CREATE TABLE `biz_task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `task_code` VARCHAR(50) NOT NULL COMMENT '任务编号',
    `task_name` VARCHAR(100) NOT NULL COMMENT '任务名称',
    `task_type` VARCHAR(50) DEFAULT NULL COMMENT '任务类型',
    `device_id` BIGINT DEFAULT NULL COMMENT '设备ID',
    `product_id` BIGINT DEFAULT NULL COMMENT '产品ID',
    `task_status` CHAR(1) DEFAULT '0' COMMENT '任务状态（0待开始 1进行中 2已完成 3已取消）',
    `progress` INT DEFAULT 0 COMMENT '进度（0-100）',
    `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
    `actual_start_time` DATETIME DEFAULT NULL COMMENT '实际开始时间',
    `actual_end_time` DATETIME DEFAULT NULL COMMENT '实际结束时间',
    `operator` VARCHAR(50) DEFAULT NULL COMMENT '操作人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_code` (`task_code`),
    KEY `idx_device_id` (`device_id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务表';

-- =============================================
-- 告警表
-- =============================================
DROP TABLE IF EXISTS `biz_alarm`;
CREATE TABLE `biz_alarm` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '告警ID',
    `alarm_code` VARCHAR(50) NOT NULL COMMENT '告警编号',
    `alarm_type` VARCHAR(50) DEFAULT NULL COMMENT '告警类型',
    `alarm_level` VARCHAR(20) DEFAULT NULL COMMENT '告警级别（低 中 高 紧急）',
    `device_id` BIGINT DEFAULT NULL COMMENT '设备ID',
    `alarm_content` VARCHAR(500) DEFAULT NULL COMMENT '告警内容',
    `alarm_status` CHAR(1) DEFAULT '0' COMMENT '告警状态（0未处理 1已处理）',
    `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `handler` VARCHAR(50) DEFAULT NULL COMMENT '处理人',
    `handle_remark` VARCHAR(500) DEFAULT NULL COMMENT '处理备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_alarm_code` (`alarm_code`),
    KEY `idx_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='告警表';

-- =============================================
-- 初始化数据
-- =============================================

-- 插入默认管理员用户（密码：admin123，使用BCrypt加密）
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `email`, `phone`, `status`, `create_by`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 'admin@example.com', '13800138000', '1', 'system');

-- 插入测试用户
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `email`, `phone`, `status`, `create_by`) VALUES
('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '测试用户', 'test@example.com', '13800138001', '1', 'system');

-- 插入角色
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `status`, `sort`, `create_by`) VALUES
(1, 'ROLE_ADMIN', '管理员', '1', 1, 'system'),
(2, 'ROLE_USER', '普通用户', '1', 2, 'system');

-- 插入菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `menu_code`, `path`, `component`, `icon`, `permission`, `sort`, `visible`, `status`, `create_by`) VALUES
-- 系统管理
(1, 0, '系统管理', 'M', 'system', '/system', NULL, 'system', NULL, 1, '1', '1', 'system'),
(2, 1, '用户管理', 'C', 'user', '/system/user', 'system/user/index', 'user', 'system:user:list', 1, '1', '1', 'system'),
(3, 1, '角色管理', 'C', 'role', '/system/role', 'system/role/index', 'role', 'system:role:list', 2, '1', '1', 'system'),
(4, 1, '菜单管理', 'C', 'menu', '/system/menu', 'system/menu/index', 'menu', 'system:menu:list', 3, '1', '1', 'system'),
-- 设备管理
(5, 0, '设备管理', 'M', 'device', '/device', NULL, 'device', NULL, 2, '1', '1', 'system'),
(6, 5, '设备列表', 'C', 'device-list', '/device/list', 'device/list/index', 'list', 'device:list', 1, '1', '1', 'system'),
-- 产品管理
(7, 0, '产品管理', 'M', 'product', '/product', NULL, 'product', NULL, 3, '1', '1', 'system'),
(8, 7, '产品列表', 'C', 'product-list', '/product/list', 'product/list/index', 'list', 'product:list', 1, '1', '1', 'system'),
-- 任务管理
(9, 0, '任务管理', 'M', 'task', '/task', NULL, 'task', NULL, 4, '1', '1', 'system'),
(10, 9, '任务列表', 'C', 'task-list', '/task/list', 'task/list/index', 'list', 'task:list', 1, '1', '1', 'system'),
-- 告警管理
(11, 0, '告警管理', 'M', 'alarm', '/alarm', NULL, 'alarm', NULL, 5, '1', '1', 'system'),
(12, 11, '告警列表', 'C', 'alarm-list', '/alarm/list', 'alarm/list/index', 'list', 'alarm:list', 1, '1', '1', 'system'),
-- 监控大屏
(13, 0, '监控大屏', 'M', 'dashboard', '/dashboard', NULL, 'dashboard', NULL, 6, '1', '1', 'system'),
(14, 13, '实时监控', 'C', 'monitor', '/dashboard/monitor', 'dashboard/monitor/index', 'monitor', 'dashboard:monitor', 1, '1', '1', 'system');

-- 插入用户角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2);

-- 插入角色菜单关联（管理员拥有所有菜单权限）
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12), (1, 13), (1, 14);

-- 插入角色菜单关联（普通用户只有查看权限）
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(2, 5), (2, 6), (2, 7), (2, 8), (2, 9), (2, 10), (2, 11), (2, 12), (2, 13), (2, 14);

-- 插入测试设备数据
INSERT INTO `biz_device` (`device_code`, `device_name`, `device_type`, `device_model`, `manufacturer`, `location`, `status`, `create_by`) VALUES
('DEV001', '疲劳监测传感器A', '传感器', 'FS-1000', 'XX科技有限公司', '车间A区', '1', 'system'),
('DEV002', '疲劳监测传感器B', '传感器', 'FS-1000', 'XX科技有限公司', '车间B区', '1', 'system'),
('DEV003', '数据采集器', '采集器', 'DC-2000', 'XX科技有限公司', '控制室', '1', 'system'),
('DEV004', '监控摄像头', '摄像头', 'CAM-3000', 'XX科技有限公司', '车间A区', '1', 'system');

-- 插入测试产品数据
INSERT INTO `biz_product` (`product_code`, `product_name`, `product_type`, `specification`, `unit`, `quantity`, `price`, `status`, `create_by`) VALUES
('PRD001', '疲劳监测传感器', '传感器', 'FS-1000', '台', 100, 5000.00, '1', 'system'),
('PRD002', '数据采集器', '采集器', 'DC-2000', '台', 50, 8000.00, '1', 'system'),
('PRD003', '监控摄像头', '摄像头', 'CAM-3000', '台', 30, 3000.00, '1', 'system'),
('PRD004', '连接线缆', '配件', 'CABLE-10M', '根', 200, 100.00, '1', 'system');

-- 插入测试任务数据
INSERT INTO `biz_task` (`task_code`, `task_name`, `task_type`, `device_id`, `product_id`, `task_status`, `progress`, `operator`, `create_by`) VALUES
('TASK001', '设备安装任务', '安装', 1, 1, '2', 100, 'admin', 'system'),
('TASK002', '设备维护任务', '维护', 2, 1, '1', 50, 'admin', 'system'),
('TASK003', '产品入库任务', '入库', NULL, 2, '0', 0, 'admin', 'system');

-- 插入测试告警数据
INSERT INTO `biz_alarm` (`alarm_code`, `alarm_type`, `alarm_level`, `device_id`, `alarm_content`, `alarm_status`, `create_by`) VALUES
('ALM001', '设备离线', '高', 1, '设备DEV001已离线超过30分钟', '0', 'system'),
('ALM002', '数据异常', '中', 2, '设备DEV002数据采集异常', '0', 'system'),
('ALM003', '温度过高', '紧急', 3, '设备DEV003温度超过阈值', '0', 'system');

-- =============================================
-- 初始化完成
-- =============================================

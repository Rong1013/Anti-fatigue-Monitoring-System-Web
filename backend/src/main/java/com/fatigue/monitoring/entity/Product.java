package com.fatigue.monitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品实体
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Data
@TableName("product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 产品编号
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 产品规格
     */
    private String specification;

    /**
     * 产品型号
     */
    private String model;

    /**
     * 材质
     */
    private String material;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 生产日期
     */
    private LocalDateTime productionDate;

    /**
     * 保质期
     */
    private Integer shelfLife;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 仓库位置
     */
    private String warehouseLocation;

    /**
     * 产品描述
     */
    private String description;

    /**
     * 删除标志（0未删除 1已删除）
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
}

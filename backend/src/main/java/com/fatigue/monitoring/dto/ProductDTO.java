package com.fatigue.monitoring.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品DTO
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Data
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
    private Long id;

    /**
     * 产品编号
     */
    @NotBlank(message = "产品编号不能为空")
    private String productCode;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空")
    private String productName;

    /**
     * 产品类型
     */
    @NotBlank(message = "产品类型不能为空")
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
    @NotNull(message = "数量不能为空")
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
     * 备注
     */
    private String remark;
}

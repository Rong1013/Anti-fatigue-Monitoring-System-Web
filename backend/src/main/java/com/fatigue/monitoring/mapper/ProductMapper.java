package com.fatigue.monitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.monitoring.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 产品Mapper接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 分页查询产品列表
     *
     * @param page    分页对象
     * @param product 产品信息
     * @return 产品列表
     */
    IPage<Product> selectProductPage(Page<Product> page, @Param("product") Product product);

    /**
     * 根据产品编号查询产品
     *
     * @param productCode 产品编号
     * @return 产品信息
     */
    Product selectByProductCode(@Param("productCode") String productCode);
}

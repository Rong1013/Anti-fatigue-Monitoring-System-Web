package com.fatigue.monitoring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fatigue.monitoring.entity.Product;

/**
 * 产品服务接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
public interface ProductService extends IService<Product> {

    /**
     * 分页查询产品列表
     *
     * @param page    分页参数
     * @param product 查询条件
     * @return 产品分页列表
     */
    Page<Product> selectProductPage(Page<Product> page, Product product);

    /**
     * 根据产品编号查询产品
     *
     * @param productCode 产品编号
     * @return 产品信息
     */
    Product selectByProductCode(String productCode);

    /**
     * 创建产品
     *
     * @param product 产品信息
     * @return 是否成功
     */
    boolean createProduct(Product product);

    /**
     * 更新产品
     *
     * @param product 产品信息
     * @return 是否成功
     */
    boolean updateProduct(Product product);

    /**
     * 删除产品
     *
     * @param productId 产品ID
     * @return 是否成功
     */
    boolean deleteProduct(Long productId);

    /**
     * 更新产品状态
     *
     * @param productId 产品ID
     * @param status    状态
     * @return 是否成功
     */
    boolean updateStatus(Long productId, Integer status);

    /**
     * 减少产品数量
     *
     * @param productId 产品ID
     * @param quantity  数量
     * @return 是否成功
     */
    boolean reduceQuantity(Long productId, Integer quantity);

    /**
     * 增加产品数量
     *
     * @param productId 产品ID
     * @param quantity  数量
     * @return 是否成功
     */
    boolean addQuantity(Long productId, Integer quantity);
}

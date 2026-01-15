package com.fatigue.monitoring.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.monitoring.common.result.Result;
import com.fatigue.monitoring.entity.Product;
import com.fatigue.monitoring.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 产品控制器
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 分页查询产品列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @param product 查询条件
     * @return 产品分页列表
     */
    @GetMapping("/page")
    public Result<Page<Product>> selectProductPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            Product product) {
        Page<Product> page = new Page<>(current, size);
        return Result.success(productService.selectProductPage(page, product));
    }

    /**
     * 根据ID查询产品
     *
     * @param id 产品ID
     * @return 产品信息
     */
    @GetMapping("/{id}")
    public Result<Product> selectById(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    /**
     * 创建产品
     *
     * @param product 产品信息
     * @return 是否成功
     */
    @PostMapping
    public Result<Boolean> createProduct(@Validated @RequestBody Product product) {
        return Result.success(productService.createProduct(product));
    }

    /**
     * 更新产品
     *
     * @param product 产品信息
     * @return 是否成功
     */
    @PutMapping
    public Result<Boolean> updateProduct(@Validated @RequestBody Product product) {
        return Result.success(productService.updateProduct(product));
    }

    /**
     * 删除产品
     *
     * @param id 产品ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteProduct(@PathVariable Long id) {
        return Result.success(productService.deleteProduct(id));
    }

    /**
     * 更新产品状态
     *
     * @param id     产品ID
     * @param status 状态
     * @return 是否成功
     */
    @PutMapping("/status")
    public Result<Boolean> updateStatus(
            @RequestParam Long id,
            @RequestParam String status) {
        return Result.success(productService.updateStatus(id, status));
    }

    /**
     * 减少库存
     *
     * @param id       产品ID
     * @param quantity 数量
     * @return 是否成功
     */
    @PutMapping("/reduce")
    public Result<Boolean> reduceQuantity(
            @RequestParam Long id,
            @RequestParam Integer quantity) {
        return Result.success(productService.reduceQuantity(id, quantity));
    }

    /**
     * 增加库存
     *
     * @param id       产品ID
     * @param quantity 数量
     * @return 是否成功
     */
    @PutMapping("/add")
    public Result<Boolean> addQuantity(
            @RequestParam Long id,
            @RequestParam Integer quantity) {
        return Result.success(productService.addQuantity(id, quantity));
    }
}
